import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorFileManager {
    private String errorFilePath;
    private String errorMessage;
    private String outputErrFile;

    public ErrorFileManager(String errorFilePath, String errorMessage, String outputErrFile) {
        this.errorFilePath = errorFilePath;
        this.errorMessage = errorMessage;
        this.outputErrFile = outputErrFile;
    }

    public void checkErrorFileExists() {
        File errorFile = new File(errorFilePath);
        if (!errorFile.exists()) {
            createErrorFile(errorMessage);
            throw new BatRuntimeException(errorMessage);
        }
    }

    public void createErrorFile(String errorMessage) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(errorFilePath + outputErrFile, true); // 追記モードでファイルを開く
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(errorMessage);
            bufferedWriter.newLine(); // 改行を追加
        } catch (IOException e) {
            // エラーログの書き込みに失敗した場合の処理
            e.printStackTrace();
        } finally {
            // リソースをクローズする
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
