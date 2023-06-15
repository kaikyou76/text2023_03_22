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
    
    @Scheduled(cron = "0 0 23 * * ?") // 毎日夜11時に実行
    public void launchShellScript() {
        // ジョブの実行コードをここに記述
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();
        
        try {
            JobExecution jobExecution = jobLauncher.run(csvADExportJob, jobParameters);
            System.out.println("Job execution status: " + jobExecution.getStatus());
            
            // シェルスクリプトの実行コードをここに記述
            // 例: Runtime.getRuntime().exec("/bin/sh /path/to/csvADExport.sh");
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
}
#########################################################################################
上記の例では、launchShellScriptメソッドが@Scheduledアノテーションで定期的に実行されるように設定されています。
メソッド内でジョブを実行し、その後にシェルスクリプトを実行しています。

なお、シェルスクリプトの実行コード部分はコメントアウトされており、実際のシェルスクリプトのパスと実行方法に合わせて適切に修正してください。

補充：
runCsvADExportJobクラスはSpring Batch環境で自動実行されるコンポーネントです。@Scheduledアノテーションを使用して、指定されたスケジュール（cron式）に基づいて定期的に実行されます。

このクラスはCsvADExportJobというSpringコンポーネントとして定義されており、JobLauncherとJobが自動的に注入されます。launchShellScriptメソッド内でジョブの実行とシェルスクリプトの実行が行われます。

ジョブの実行にはJobLauncherを使用し、指定されたcsvADExportJobとジョブパラメータを渡して実行します。また、シェルスクリプトの実行にはRuntime.getRuntime().exec()を使用しています。

したがって、runCsvADExportJobクラスはSpring Batch環境で自動実行され、ジョブの実行とシェルスクリプトの実行が同じクラス内で制御されています。