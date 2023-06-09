import jp.co.ksc.batch.util.CSVException;
import jp.co.ksc.batch.util.CSVReadUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
/**
*以下は、指定されたCSVReadUtilを使用してorganization.csvを読み込むコード例です
*/
public class Main {
    public static void main(String[] args) {
        String fileName = "organization.csv";
        int colCount = 8;

        CSVReadUtil csvReadUtil = new CSVReadUtil();

        try {
            List<String[]> csvData = csvReadUtil.read(fileName, colCount);

            // 読み込んだCSVデータの処理
            for (String[] row : csvData) {
                for (String cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CSVException e) {
            e.printStackTrace();
        }
    }
}
