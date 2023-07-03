public class MyClass {
    public void someMethod(BatchSettings props) {
        String errorFilePath = props.getOutPutErrFile();
        String errorMessage = props.getErrMessage();
		String outputErrFile = props.getOutputErrFile();
        ErrorFileManager errorFileManager = new ErrorFileManager(errorFilePath, errorMessage, outputErrFile);

        try {
            errorFileManager.checkErrorFileExists();
            // エラーファイルが存在する場合の処理
        } catch (BatRuntimeException e) {
            // エラーファイルが存在しない場合の処理
            e.printStackTrace();
        }

        try {
            errorFileManager.createErrorFile();
            // エラーファイルの作成成功時の処理
        } catch (BatRuntimeException e) {
            // エラーファイルの作成失敗時の処理
            e.printStackTrace();
        }
    }
}
