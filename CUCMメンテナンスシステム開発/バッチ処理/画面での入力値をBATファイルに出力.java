import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class InputToBatFileExample {

    private static final String BAT_FILE_PATH = "/path/to/output/file.bat"; // BATファイルの出力先パス

    public static void main(String[] args) {
        try {
            // 画面からの入力を受け取る
            Scanner scanner = new Scanner(System.in);
            System.out.print("入力値を入力してください: ");
            String input = scanner.nextLine();

            // BATファイルに入力値を出力
            writeInputToBatFile(input);

            // ファイルの書き込みが完了したことを通知
            System.out.println("BATファイルに入力値を出力しました。");
        } catch (IOException e) {
            System.out.println("ファイルの書き込み中にエラーが発生しました。");
            e.printStackTrace();
        }
    }

    private static void writeInputToBatFile(String input) throws IOException {
        FileWriter fileWriter = new FileWriter(BAT_FILE_PATH);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("@echo off");
        printWriter.println("echo " + input);

        printWriter.close();
    }
}
