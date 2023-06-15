import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CsvADExportJob {
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private Job csvADExportJob;
    
    // ファイル移動のためのディレクトリ設定
    private String inputDir = "InputDir"; // 変数A
    private String receiveDir = "ReceiveDir"; // 変数L--連携ファイルディレクトリ
    
    // 連携ファイルの移動
    private void moveFiles() {
        File sourceDirectory = new File(receiveDir);
        File[] files = sourceDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    File destination = new File(inputDir + File.separator + file.getName());
                    Files.move(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Scheduled(cron = "0 0 23 * * ?") // 毎日夜11時に実行
    public void launchShellScript() {
        // 連携ファイルの移動
        moveFiles();
        
        // 必須CSVファイルとEOFファイルの存在チェック
        String csvDir = inputDir; // CSVファイルの格納ディレクトリは変数A
        String eofAdFile = csvDir + File.separator + "EOFAD"; // EOFADファイル名は変数D
        String eofAmFile = csvDir + File.separator + "EOFAM"; // EOFAMファイル名は変数E
        String csvAdFile = csvDir + File.separator + "organization.csv"; // 組織情報ファイル名は変数F
        String csvDepartmentFile = csvDir + File.separator + "department.csv"; // 店部課情報ファイル名は変数G
        String csvEmployeeFile = csvDir + File.separator + "employee.csv"; // 社員情報ファイル名は変数H

        if (!checkFileExists(eofAdFile) || !checkFileExists(eofAmFile) ||
                !checkFileExists(csvAdFile) || !checkFileExists(csvDepartmentFile) || !checkFileExists(csvEmployeeFile)) {
            String errorMessage = "存在しないCSV/EOFファイル名"; // メッセージID:BT_000_E002に相当するエラーメッセージ
            System.out.println(errorMessage);
            throw new BatRuntimeException(errorMessage);
        }

        // ジョブの実行コードをここに記述
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();
        
        try {
            JobExecution jobExecution = jobLauncher.run(csvADExportJob, jobParameters);
            System.out.println("Job execution status: " + jobExecution.getStatus());
            
            // シェルスクリプトの実行コードをここに記述
            Process process = Runtime.getRuntime().exec("/bin/sh /path/to/csvADExport.sh");
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("Shell script execution succeeded");
            } else {
                System.out.println("Shell script execution failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private static class BatRuntimeException extends RuntimeException {
        public BatRuntimeException(String message) {
            super(message);
        }
    }
}
