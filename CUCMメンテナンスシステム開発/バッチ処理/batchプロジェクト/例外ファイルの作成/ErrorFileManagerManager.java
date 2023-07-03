import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorFileManager {
    private String errorFilePath;

    public ErrorFileManager(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }

    public boolean checkErrorFileExists() {
        File file = new File(errorFilePath);
        return file.exists();
    }

    public void createErrorFile(String errorMessage) throws IOException {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(errorFilePath, true); // 追記モードでファイルを開く
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(errorMessage);
            bufferedWriter.newLine(); // 改行を追加
        } finally {
            // リソースをクローズする
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }
}
