public class CsvADExportJob {

    public void launchShellScript() {
        try {
            Process process = Runtime.getRuntime().exec("/bin/sh /path/to/csvADExport.sh");
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ジョブの実行などの設定
}
