/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * LoadPersonnelInfoWriter.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */
 
package jp.co.netmarks.batch.component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.Calendar; 
import java.util.List;
import java.util.Map;

import jp.co.ksc.batch.exception.CSVException;
import jp.co.ksc.batch.util.BatchSettings;
import jp.co.ksc.batch.util.CSVUtil;
import jp.co.netmarks.batch.dao.PersonnelInfoDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
*<pre>
*人事情報取込みロジック
*
*&lt;MODIFICATION HISTORY&gt;
*1.0 2013/09/12 KSC Hiroaki Endo
* </pre>
*
* @author KSC Hiroaki Endo
* @version 1.0 2013/09/12
*/
@Component
public class LoadPersonnelInfoLogic {
    /** ログ出力クラス */
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private PersonnelInfoDAO dao;

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

    // **********************************
    // CSV処理
    // **********************************

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
        String[] header = setting.getBizOrganizationCsvHeader().split(", ", 0);
        String tableName = setting.getBizOrganizationTableName();
        Map<String, Object>[] records = readBizCsv(setting.getCsvFtpDir() + setting.getBizOrganizationCsvFileName(), header);

        // CSVデータをBIZテーブルに登録
        if (records != null && records.length > 0) {
            log.debug(tableName + "登録対象件数:" + records.length);
            dao.insertBizTableAll(records, tableName, header);
        }
        // BIZテーブルからダミー等を削除
        dao.deleteBizOrganization();
    }

    // *********************************
    // CSV (BizDepartment)取込み
    // *********************************

    /**
     * CSV (BizDepartment)取込み
     * 
     * @param setting BatchSettings
     * @throws IOException
     * @throws CSVException
     * @throws SQLException
     */
    public void doBizDepartment(BatchSettings setting) throws IOException, CSVException, SQLException {
        // CSVファイル読込み
        String[] header = setting.getBizDepartmentCsvHeader().split(", ", 0);
        String tableName = setting.getBizDepartmentTableName();
        Map<String, Object>[] records = readBizCsv(setting.getCsvFtpDir() + setting.getBizDepartmentCsvFileName(), header);

        // CSVデータをBIZテーブルに登録
        if (records != null && records.length > 0) {
            log.debug(tableName + "登録対象件数:" + records.length);
            dao.insertBizTableAll(records, tableName, header);
        }

        // BIZテーブルからダミー等を削除
        dao.deleteBizDepartment();
    }

    // ******************************
    // CSV (BizEmployee)取込み
    // ******************************

    /**
     * CSV (BizEmployee)取込み
     * 
     * @param setting BatchSettings
     * @throws IOException
     * @throws CSVException
     * @throws SQLException
     */
    public void doBizEmployee(BatchSettings setting) throws IOException, CSVException, SQLException {
        // CSVファイル読込み
        String[] header = setting.getBizEmployeeCsvHeader().split(", ", 0);
        String tableName = setting.getBizEmployeeTableName();
        Map<String, Object>[] records = readBizCsv(setting.getCsvFtpDir() + setting.getBizEmployeeCsvFileName(), header);

        // CSVデータをBIZテーブルに登録
        if (records != null && records.length > 0) {
            log.debug(tableName + ":" + records.length);
            dao.insertBizTableAll(records, tableName, header);
        }

        // BIZテーブルからダミー等を削除
        dao.deleteBizEmployee();
    }

    // *************************
    // CSV (BizAd)取込み
    // *************************

    /**
     * CSV (BizAd)取込み
     * 
     * @param setting BatchSettings
     * @throws IOException
     * @throws CSVException
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public void doBizAd(BatchSettings setting) throws IOException, CSVException, SQLException {
        // CSVファイル読込み
        String[] header = setting.getBizAdCsvHeader().split(", ", 0);
        String tableName = setting.getBizAdTableName();
        Map<String, Object>[] records = readBizCsv(setting.getCsvFtpDir() + setting.getBizAdCsvFileName(), header);

        // CSVデータをBIZテーブルに登録
        if (records != null && records.length > 0) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            List<String> addedCodeList = new ArrayList<String>();

            // 社員コード 10桁の場合、先頭3桁を取り除く
            for (int i = 0; i < records.length; i++) {
                String employeeCode = (String) records[i].get("USER LOGON NAME");
                try {
                    Long.parseLong(employeeCode);

                    // 10桁 & 会社コード「ool」
                    if (employeeCode.length() != 7 && employeeCode.length() == 10) {
                        records[i].put("USER LOGON NAME", employeeCode.substring(employeeCode.length() - 7));
                        log.debug("社員コード変換 元社員コード:[" + employeeCode + "]新社員コード:[" + records[i].get("USER_LOGON_NAME") + "]");

                        boolean added = false;
                        for (int j = 0; j < addedCodeList.size(); j++) {
                            String tmpCode = (String) addedCodeList.get(j);
                            if (tmpCode.equals(records[i].get("USER_LOGON_NAME").toString())) {
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            log.debug("ADD EmployeeCode: " + records[i].get("USER_LOGON_NAME"));
                            list.add(records[i]);
                            addedCodeList.add(records[i].get("USER_LOGON_NAME").toString());
                        } else {
                            log.debug("ADDED EmployeeCode: " + records[i].get("USER_LOGON_NAME"));
                        }

                    }
                } catch (NumberFormatException nfe) {
                    log.debug("NG EmployeeCode:" + employeeCode);
                }
            }

            Map<String, Object>[] okRecords = (Map[]) list.toArray(new Map[list.size()]);
            log.debug(tableName + "登録対象件数:" + okRecords.length);
            dao.insertBizTableAll(okRecords, tableName, header);
        }
    }
	

    
    // ******************************** 
    // CSV (BizShift) 取込み
    // ********************************

    /**
     * CSV (BizShift)取込み
     *
     * @param setting BatchSettings
     * @throws IOException
     * @throws CSVException
     * @throws SQLException
     */
    public void doBizShift(BatchSettings setting) throws IOException, CSVException, SQLException {

        // CSVファイル読込み
        String[] header = setting.getBizShiftCsvHeader().split(", ", 0);
        String tableName = setting.getBizShiftTableName();

        Map<String, Object>[] records = null;
        String[] todayCsvFileName = { setting.getCsvFtpDir() + setting.getBizShiftCsvFileName() };
        if (existsIndispensableCsvFile(todayCsvFileName)) {
            records = readBizCsv(todayCsvFileName[0], header);
        }

        // CSVデータをBIZテーブルに登録
        if (records != null && records.length > 0) {
            log.debug(tableName + "登録対象件数:" + records.length);
            dao.insertBizTableAll(records, tableName, header);
        }
    }


    // ******************************** 
    // 閾値
    // ********************************

    /**
     * 閾値の判定
     *
     * @throws Exception
     */
    public void threshold() throws Exception {

        int thresholdValue = dao.getThreshold();
        int enterCount = dao.getEnterCount();
        int retireCount = dao.getRetireCount();
        int changeCount = dao.getChangeCount();
        int updateTotalCount = enterCount + retireCount + changeCount;

        log.debug("閾値:" + thresholdValue);
        log.debug("閾値用計測（入社数）:" + enterCount);
        log.debug("閾値用計測（退社数）:" + retireCount);
        log.debug("閾値用計測（異動数）" + changeCount);
        log.debug("閾値用計測合計:" + updateTotalCount);

        if (thresholdValue < updateTotalCount) {
            String msg = "閾値を超える更新件数が発生しました。";
            log.warn(msg);
            List<Map<String, Object>> enter = dao.getEnterEmployeeList();
            log.debug("\n入社社員一覧↓↓↓↓");
            if (enter != null && enter.size() > 0) {
                for (int i = 0; i < enter.size(); i++) {
                    String employeeCode = enter.get(i).get("employee code").toString();
                    log.debug("社員コード[" + employeeCode + "]");
                }
            } else {
                log.debug("無し");
            }

            List<Map<String, Object>> retire = dao.getRetireEmployeeList();
            log.debug("\n退社社員一覧↓↓↓↓");
            if (retire != null && retire.size() > 0) {
                for (int i = 0; i < retire.size(); i++) {
                    String employeeId = retire.get(i).get("biz_employee_id").toString();
                    log.debug("社員コード[" + employeeId + "]");
                }
            } else {
                log.debug("無し");
            }

            List<Map<String, Object>> change = dao.getChangeEmployeeList();
            log.debug("\n異動社員一覧↓↓↓↓");
            if (change != null && change.size() > 0) {
                for (int i = 0; i < change.size(); i++) {
                    String employeeId = change.get(i).get("biz employee id").toString();
                    String companyId = change.get(i).get("company id").toString();
                    String sectionId = change.get(i).get("section_id").toString();
                    log.debug("社員コード[" + employeeId + "], 会社コード[" + companyId + "], 店部課コード" + sectionId + "]");
                }
            } else {
                log.debug("無し");
            }
            
            throw new Exception(msg);
        } else {
            log.debug("閾値の範囲内の更新件数でした。[" + updateTotalCount + "/" + thresholdValue + "]件（更新件数/閾値件数）");
        }
    }


    // *******************************
    // CSVファイルリネーム
    // *******************************

    /**
     * CSVファイルリネーム
     *
     * @param props BatchSettings
     * @throws IOException
     */
    public void csvFileRename(BatchSettings props) throws IOException {
        String impcsvPath = props.getCsvFtpDir();
        String impcmpcsvPath = props.getInputCompDir();
        String[] impcsv = {
            props.getEofAd(),
            props.getEofAm(),
            props.getBizAdCsvFileName(),
            props.getBizDepartmentCsvFileName(),
            props.getBizEmployeeCsvFileName(),
            props.getBizOrganizationCsvFileName(),
            props.getBizShiftCsvFileName()
        };
        File backupDir = new File(impcmpcsvPath);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
		
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String ymdhms = sdf.format(cal.getTime());		

        for (int i = 0; i < impcsv.length; i++) {
            String todayCsvFileName = impcsv[i];
            String[] csvSplit = todayCsvFileName.split("\\.", 0);
            File importCsvFile = new File(impcsvPath + todayCsvFileName);
            if (importCsvFile.exists()) {
                String newFilenm = "";
                if (i < 2) {
                    newFilenm = backupDir + "/" + todayCsvFileName + "_IMPORTED_" + ymdhms;
                } else {
                    newFilenm = backupDir + "/" + csvSplit[0] + "_IMPORTED_" + ymdhms + "." + csvSplit[1];
                }
                File renameCsvFile = new File(newFilenm);
                importCsvFile.renameTo(renameCsvFile);
            }
        }
    }

	//退職者リスト出力
    public void retiredUserFileOut(BatchSettings props) throws Exception {
        String outputPath = props.getOutPutRetireDir();
        String retireListNm = props.getRetiredUserFileName();

        File backupDir = new File(outputPath);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        try {
            Map<String, Object>[] retire = dao.selRetireUser();

            CSVUtil csv = new CSVUtil(retire);
            csv.setDataMaps(retire);
            csv.setHasHeader(true);
            csv.addHeader("last_name");
            csv.addHeader("first_name");
            csv.addHeader("telephone_number");
            csv.addHeader("full_name");
            csv.setFileName(outputPath + csv.getTimestampAddFileNM(retireListNm));
            csv.write();
        } catch (Exception e) {
            throw e;
        }
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
