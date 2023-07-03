public class MyClass {
    public void someMethod() {
        String errorFilePath = "パス/エラーファイル.txt";
        ErrorFileManager errorFileManager = new ErrorFileManager(errorFilePath);

        // エラーファイルの存在確認
        boolean exists = errorFileManager.checkErrorFileExists();
        if (exists) {
            System.out.println("エラーファイルが存在します");
        } else {
            System.out.println("エラーファイルは存在しません");
        }

        // エラーファイルの作成
        try {
            errorFileManager.createErrorFile("エラーメッセージ1");
            System.out.println("エラーファイルにエラーメッセージを書き込みました");
        } catch (IOException e) {
            System.out.println("エラーファイルの作成に失敗しました");
            e.printStackTrace();
        }
    }
}
