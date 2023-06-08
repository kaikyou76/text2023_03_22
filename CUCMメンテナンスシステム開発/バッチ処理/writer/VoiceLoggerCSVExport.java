package jp.co.ksc.batch.step.writer;

import jp.co.ksc.batch.exception.BatRuntimeException;
import jp.co.ksc.batch.util.BatchSettings;
import jp.co.ksc.batch.util.CSVUtil;
import jp.co.netmarks.batch.persistence.CSVExpImpMapper;
import jp.co.ksc.batch.util.LockFileManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * VoiceLoggerをCSVへエクスポートするクラスです。
 *
 * <pre>
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/09/12 KSC Hiroaki Endo # *
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
@Component
public class VoiceLoggerCSVExport implements ItemWriter<Object> {
    private static final Log log = LogFactory.getLog(VoiceLoggerCSVExport.class);

    @Autowired
    private Properties properties;

    @Autowired
    private CSVExpImpMapper csvMapper;

    /**
     * VoiceLoggerをCSVへエクスポートします。
     *
     * @param paramList パラメータリスト
     * @throws Exception 例外
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
            // データ取得
            Map<String, Object> expVL = csvMapper.getVoiceLoggerExp();
            String[] header = bs.getVoiceOsvHeader().split(",",0);
            
            // 出力
            CSVUtil csv = new CSVUtil(expVL);
            csv.setHasHeader(true);
            csv.addHeader(header[0].toLowerCase());
            csv.addHeader(header[1].toLowerCase());
            csv.setFileName(bs.getOutputDirAssociate() + csv.getExpTimeAddFileNM(bs.getExportVoice()));
            File backupDir = new File(bs.getOutputDirAssociate());
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            
            csv.write();
        } catch (Exception e) {
            throw e;
        } finally {
            // ロック解除
            LockFileManager.unlock(bs);
        }
    }
}
この修正では、次の変更が行われています：

Voice LoggerCSVExport クラスは @Component アノテーションでコンポーネントとしてマークされ、Springのコンポーネントスキャンによって自動的に検出されるようになりました。
