/**
 * VoiceLoggerCSVImport.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */
package jp.co.ksc.batch.step.writer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.co.ksc.batch.exception.BatRuntimeException;
import jp.co.ksc.batch.exception.CSVException;
import jp.co.ksc.batch.util.BatchSettings;
import jp.co.ksc.batch.util.CSVUtil;
import jp.co.ksc.batch.util.LockFileManager;
import jp.co.netmarks.batch.persistence.CSVExpImpMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * VoiceLoggerCSVImportor
 *
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo #EX
 * </pre>
 *
 * @version 1.0 2013/09/12
 */
public class VoiceLoggerCSVImport implements ItemWriter<Object> {
    private static final Log log = LogFactory.getLog(VoiceLoggerCSVImport.class);

    @Autowired
    private Properties properties;
    @Autowired
    private CSVExpImpMapper csvMapper;

    /**
     * VoiceLoggerCSVをインポートします
     * 
     * @param paramList
     * @throws Exception
     */
    @Override
    public void write(List<?> paramList) throws Exception {
        BatchSettings bs = new BatchSettings(properties);
        
        // ロックファイルを確認
        try {
            LockFileManager.lock(bs);
        } catch (IOException ex) {
            throw new BatRuntimeException(ex.getMessage(), ex);
        }

        try {
            String impCsvPath = bs.getOsvFtpDir();
            String fileVoice = bs.getImportVoice();
            String impCsvFilePath = bs.getInputCompDir();
            
            CSVUtil csvUtil = new CSVUtil(fileVoice);
            String importedFile = csvUtil.getImpTimeAddFileNM(fileVoice);
            
            File impCsvFile = new File(impCsvPath + fileVoice);
            boolean isFileExist = impCsvFile.isFile();
            
            if (!isFileExist) {
                log.debug("CSVファイルが存在しません。");
                log.debug("CSVファイルパス: " + impCsvPath + fileVoice);
                LockFileManager.unlock(bs);
            } else {
                // CSVファイル読込み
                String[] header = bs.getVoiceCsvHeader().split("\\.", 0);
                String tableName = bs.getVoiceTableName();
                Map<String, Object> records = readVoiceCsv(impCsvFile.toString(), header);
                
                // CSVデータをテーブルに登録
                if (records != null && records.size() > 0) {
                    log.debug(tableName + "VoiceCSV件数: " + records.size());
                    insertVoiceTable(records, tableName, header);
                }
                
                // ファイルのリネーム
                File backupDir = new File(impCsvFilePath);
                if (!backupDir.exists()) {
                    backupDir.mkdirs();
                }
                File impNewCsvFile = new File(impCsvFilePath + "/" + importedFile);
                impCsvFile.renameTo(impNewCsvFile);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            LockFileManager.unlock(bs);
        }
    }

    /**
     * VoiceLoggerCSVファイルの読み込みを行います
     * 
     * @param impCsvFile
     *            取込ファイル
     * @param csvHeader
     * @return SVO Map
     * @throws CSVException
     * @throws IOException
     */
    private Map<String, Object> readVoiceCsv(String impCsvFile, String csvHeader) throws CSVException, IOException {
        CSVUtil csvUtil = new CSVUtil(impCsvFile);
        csvUtil.setHasHeader(false);

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
            log.debug("CSV読み込み完了: " + impCsvFile);
        } catch (IOException ioe) {
            log.warn("CSV読み込みエラー: " + impCsvFile);
            throw ioe;
        } catch (CSVException csve) {
            log.warn("CSV読み込みエラー: " + impCsvFile);
            throw csve;
        }

        return csvUtil.getDataMaps();
    }

    /**
     * VoiceLoggerテーブルへの挿入を行います。
     * 
     * @param records
     * @param tableName
     * @param header
     * @throws SQLException
     */
    public void insertVoiceTable(Map<String, Object> records, String tableName, String[] header) throws SQLException {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> selResult = new HashMap<String, Object>();
        String loggerData = "";
        int addedCnt = 0;

        if (records != null) {
            try {
                for (int i = 0; i < records.size(); i++) {
                    try {
                        Integer.parseInt((String) records.get(header[0]));
                    } catch (NumberFormatException ne) {
                        log.warn("Directory NumberのCSVフォーマットエラー");
                        continue;
                    }
                    
                    param.put("directorynumber", records.get(header[0]));
                    selResult = csvMapper.selVoiceDN(param);
                    
                    if (selResult.size() == 0) {
                        log.warn("VoiceLogger CSV Directory Numberエラー");
                        log.warn("DirectoryNumber: " + records.get(header[0]) + " ");
                        continue;
                    }
                    
                    addedCnt = csvMapper.selVoiceID(selResult);
                    param.clear();
                    param.put("CUCM_LINE_ID", selResult.get("cucm_line_id"));
                    param.put("STATUS_CODE", null);
                    param.put("ASSOCIATE_CODE", null);
                    
                    loggerData = (String) records.get(header[1]);
                    
                    switch (loggerData) {
                        case "0":
                        case "none":
                            loggerData = "0";
                            break;
                        case "1":
                        case "all":
                            loggerData = "1";
                            break;
                        default:
                            loggerData = "0";
                    }
                    
                    param.put(header[1], loggerData);
                    
                    if (addedCnt == 0) {
                        csvMapper.insertVoiceLogger(param);
                    } else {
                        csvMapper.updateVoiceLogger(param);
                    }
                }
            } catch (Exception e) {
                log.warn("VoiceLoggerテーブル挿入エラー: " + tableName);
                throw e;
            }
        }
    }
}
