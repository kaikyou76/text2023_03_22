package jp.co.batch.component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.batch.dao.StaffInfoDAO;
import jp.co.batch.exception.CSVException;
import jp.co.batch.util.BatchSettings;
import jp.co.batch.util.CSVUtil;

/**
 *
 * <p>Title: LoadStaffInfoLogic.java</p>
 * Jun 25, 2023
 *@author Yao KaiKyou
 */
public class LoadStaffInfoLogic {
    /** ログ出力クラス */
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private StaffInfoDAO dao;

    // ***********************************
    // 必須 CSVファイル存在チェック
    // ***********************************

    /**
     * 必須 CSVファイル存在チェック
     *
     * @param fileNames
     * @return ALLあり True以外 False
     */
    public boolean existsIndispensableCsvFile(String[] fileNames) {
        boolean ret = true;

        for (int i = 0; i < fileNames.length; i++) {
            File file = new File(fileNames[i]);
            ret = file.isFile();
            if (!ret) {
                log.debug("CSVファイル[" + fileNames[i] + "]は存在しません。");
                return ret;
            }
        }
        return ret;
    }

    /**
     * CSV (ORGANIZATION)取込み
     *
     * @param setting BatchSettings
     * @throws IOException
     * @throws CSVException
     * @throws SQLException
     */
    public void doBizOrganization(BatchSettings setting) throws IOException, CSVException, SQLException {
        // CSVファイル読込み
        String[] header = setting.getTmpBizOrganizationCsvHeader().split(", ", 0);
        String tableName = setting.getTmpBizOrganizationTableName();
        Map<String, Object>[] records = readBizCsv(setting.getCsvFtpDir() + setting.getTmpIntOrganizationCsvFileName(), header);

        // CSVデータをBIZテーブルに登録
        if (records != null && records.length > 0) {
            log.debug(tableName + "登録対象件数:" + records.length);
            dao.insertBizTableAll(records, tableName, header);
        }
        // BIZテーブルからダミー等を削除
        dao.deleteBizOrganization();
    }

  //*********************************
 // 内部処理メソッド
 //*********************************
 /**
 * 内部処理メイン
 *
 *@param todayCsvFileName CSV)71K
 *@param csvHeader A
 *@return 読み込んだ csvの内容
 *@throws IOException
 *@throws CSVException
 */
 private Map<String, Object>[] readBizCsv(String todayCsvFileName, String[] csvHeader)
             throws IOException, CSVException {
         CSVUtil csvUtil = new CSVUtil(todayCsvFileName);

         if (csvHeader != null) {
             for (int i = 0; i < csvHeader.length; i++) {
                 csvUtil.addHeader(csvHeader[i]);
             }
         } else {
             log.warn("CSVファイルのヘッダーが定義されていません。");
             throw new CSVException();
         }

         try {
             csvUtil.read();
             log.debug("CSVファイル[" + todayCsvFileName + "]読込み成功");
         } catch (IOException ioe) {
             log.warn("CSVファイル[" + todayCsvFileName + "]読込みエラー(IOException)");
             throw ioe;
         } catch (CSVException csve) {
             log.warn("CSVファイル[" + todayCsvFileName + "]読込みエラー(CSVException)");
             throw csve;
         }

         return csvUtil.getDataMaps();
     }

}
