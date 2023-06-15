/*
 * AppCSVImport.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.ksc.batch.step.writer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.co.ksc.batch.exception.BatRuntimeException;
import jp.co.ksc.batch.exception.CSVException;
import jp.co.ksc.batch.util.BatchSettings;
import jp.co.ksc.batch.util.LockFileManager;
import jp.co.netmarks.batch.persistence.CSVExpImpMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 全テーブル一括CSV取込
 *
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class AppCSVImport implements ItemWriter<Object> {
    private static final Log log = LogFactory.getLog(AppCSVImport.class);

    @Autowired
    private Properties properties;

    @Autowired
    private CSVExpImpMapper csvMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    /**
     * 全テーブル一括でCSVを取込みます
     *
     * @param paramList
     * @throws Exception
     */
    @Override
    public void write(List<?> paramList) throws Exception {
        DefaultTransactionDefinition dtd = new DefaultTransactionDefinition();
        dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        dtd.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        TransactionStatus ts = null;
        BatchSettings bs = new BatchSettings(properties);

        // ロックファイルを確認
        try {
            LockFileManager.lock(bs);
        } catch (IOException ex) {
            throw new BatRuntimeException(ex.getMessage(), ex);
        }

        try {
            File backupDir = new File(bs.getOutputDirDB());
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            String[] allTables = bs.getAllTableName().split(",");
            String inputDir = bs.getCsvFtpDir();
            Map<String, Object> param = new HashMap<String, Object>();
            int errcnt = 0;

            if (allTables != null) {
                for (int i = 0; i < allTables.length; i++) {
                    param.put("TABLENM", allTables[i]);
                    //[Dir/tablenm.csv]
                    String pathFileNm = inputDir + allTables[i] + ".csv";
                    File imposv = new File(pathFileNm);

                    if (!imposv.exists()) {
                        log.info(pathFileNm + " CSVファイルが存在しません");
                        continue;
                    }

                    pathFileNm = pathFileNm + param.put("INPUTNM", pathFileNm);

                    try {
                        ts = txManager.getTransaction(dtd);
                        csvMapper.allTableImport(param);
                        txManager.commit(ts);
                    } catch (Exception se) {
                        errcnt++;
                        txManager.rollback(ts);
                        log.error(se.getMessage());
                        continue;
                    }
                }

                if (errcnt > 0) {
                    log.warn("【エラーが発生しているテーブルがあります】");
                }
            } else {
                log.warn("CSVファイルの定義がされていません。");
                throw new CSVException();
            }
        } catch (Exception e) {
            // ロック解除
            LockFileManager.unlock(bs);
            throw e;
        } finally {
            // ロック解除
            LockFileManager.unlock(bs);
        }
    }
}
