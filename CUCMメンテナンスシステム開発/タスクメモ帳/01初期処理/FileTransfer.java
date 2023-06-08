import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileTransfer {

    public static void main(String[] args) {
        // ファイル移動のためのディレクトリ設定
        String inputDir = "InputDir"; // 変数A
        String receiveDir = "ReceiveDin"; // 変数L--連携ファイルディレクトリ (AD及び統合IDシステムから連携されたファイルの配置フォルダ)

        // 連携ファイルの移動
        moveFiles(receiveDir, inputDir);

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
    }

    private static void moveFiles(String sourceDir, String destinationDir) {
        File sourceDirectory = new File(sourceDir);
        File[] files = sourceDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    File destination = new File(destinationDir + File.separator + file.getName());
                    Files.move(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private static class BatRuntimeException extends RuntimeException {
        public BatRuntimeException(String message) {
            super(message);
        }
    }
}

上記springMVC対応のコードは以下の仕様を実現しました。

設定項目名：InputDir
説明：処理対象ファイル格納ディレクトリ
保持方法：変数A

設定項目名：InputCompDir
説明：退避ファイル格納先ディレクトリ
保持方法：B

設定項目名：OutputRetireDir
説明：退職者・加入者情報データ格納先ディレクトリ
保持方法：C

設定項目名：EofAd
説明：EOFADファイル名
保持方法：D

設定項目名：EofAm
説明：EOFAMファイル名
保持方法：E

設定項目名：TmpAdCsvFileName
説明：BizAdのCSVファイル名
保持方法：F

設定項目名：TmpIntDepartmentCsvFileName
説明：BizDepartment のCSVファイル名
保持方法：G

設定項目名：TmpIntEmployeeСsvFileName
説明：BizEmployee のCSVファイル名
保持方法：H

設定項目名：RetiredUserFileName
説明：退職者情報データファイル名
保持方法：J

設定項目名：JoinedUserFileName
説明：加入者情報データファイル名
保持方法：K

設定項目名：ReceiveDin
説明：連携ファイルディレクトリ (AD及び統合IDシステムから連携されたファイルの配置フォルダ)
保持方法：L

■連携ファイルの移動
連携ファイルディレクトリ内のすべてのファイルを処理対象ファイル格納ディレクトリに移動する
変数Lのディレクトリ内のファイルリストを取得する
◎ファイルリストのインデックスが先頭から末尾の間繰り返し処理を行う
・ファイルリストのファイルを変数Aの階層へコピーする
・コピーしたファイルを削除する
ここまで、ファイルリストのインデックスが先頭から末尾の間繰り返し処理を行う
必須CSVファイル存在チェック
「組織情報（organization.csv）」「店部課情報（department.csv）」「社員情報（employee.csv、）」 「AD情報（ad.csv）」 のCSVファイルおよび、「EOFAD」 「EOFAM」のEOFファイルが存在するかチェックする
CSVファイル/EOFファイルの格納フォルダは変数Aを参照
CSVファイルは変数F~Hを参照
EOFファイルは変数D/Fを参照
▼CSVファイルもしくはEOFファイルが存在しない場合
・以下のメッセージIDでログを出力する
メッセージID:BT_000_E002
パラメータ: 存在しないCSV/EOFファイル名
・「BatRuntimeException」 例外を発生させる





