/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * ConsistencyWriter.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */
package jp.co.ksc.batch.step.writer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.co.ksc.batch.exception.BatRuntimeException;
import jp.co.ksc.batch.logic.ConsistencyManager;
import jp.co.ksc.batch.util.BatchSettings;
import jp.co.ksc.batch.util.LockFileManager;
import jp.co.netmarks.batch.persistence.ConsistencyMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ConsistencyWriter class implementation of ItemWriter interface.
 * This writer is responsible for writing data to the target system.
 *
 * <pre>
 * MODIFICATION HISTORY
 * Version Date        Author           Description
 * 1.0     2013/09/12  KSC Hiroaki Endo Initial version
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class ConsistencyWriter implements ItemWriter<Object> {
    private static final Log log = LogFactory.getLog(ConsistencyWriter.class);
    private ConsistencyManager manager = new ConsistencyManager();

    @Autowired
    private Properties properties;

    @Autowired
    private ConsistencyMapper conMapper;

    private Map<String, Object> cPhoneParameter = new HashMap<>();
    private Map<String, Object> cPhoneLineParameter = new HashMap<>();
    private Map<String, Object> cLineParameter = new HashMap<>();
    private Map<String, Object> parameterValues = new HashMap<>();

    /**
     * Writes items to the target system.
     *
     * @param paramList the list of items to be written.
     * @throws Exception if there is any error during the write process.
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
            log.info("双方向データ取得開始");

            // 取得前に各テーブルをクリア
            conMapper.deleteConsistency(parameterValues);

            log.info("双方向データ取得開始 クラスタ情報の取得");
            // クラスタ情報の取得
            List<Map<String, Object>> hostList = conMapper.selectCluster(parameterValues);

            log.info("双方向データ取得開始 取得してList化");
            // CUCMから各マスタパラメータを取得してList化
            String typeInStr = properties.getProperty("typemodel.name");
            String[] typeModel = typeInStr.split(",", 0);
            typeInStr = "'";
            for (int i = 0; i < typeModel.length; i++) {
                if (i != 0)
                    typeInStr += "','";
                typeInStr += typeModel[i] + "'";
            }
            List<List<Map<String, Object>>> param = manager.start(hostList, typeInStr);

            log.info("双方向データ取込開始");
            // クラスタ毎に取得したテーブル値を加工し、Insert
            for (int id = 0; id < hostList.size(); id++) {
                int cnt = 0 + (3 * id);

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setCPhoneParameter(param.get(cnt).get(i));
                    conMapper.insertCPhone(cPhoneParameter);
                }
                cnt++;

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setCPhoneLineParameter(param.get(cnt).get(i));
                    conMapper.insertCPhoneLine(cPhoneLineParameter);
                }

                cnt++;

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setCLineParameter(param.get(cnt).get(i));
                    conMapper.insertCLine(cLineParameter);
                }
            }

            // 成功日時の保存
            int lupdCnt = conMapper.lastUpdMaster();
            if (lupdCnt == 0)
                conMapper.insLastUpdMaster();

            log.info("双方向データ取得終了");
        } catch (Exception e) {
            throw e;
        } finally {
            // ロック解除
            LockFileManager.unlock(bs);
        }
    }

    /**
     * Sets the parameter values.
     *
     * @param parameterValues the parameter values to be set.
     */
    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * Sets the cPhoneParameter.
     *
     * @param cPhoneParameter the cPhoneParameter to be set.
     */
    public void setCPhoneParameter(Map<String, Object> cPhoneParameter) {
        this.cPhoneParameter = cPhoneParameter;
    }

    /**
     * Sets the cPhoneLineParameter.
     *
     * @param cPhoneLineParameter the cPhoneLineParameter to be set.
     */
    public void setCPhoneLineParameter(Map<String, Object> cPhoneLineParameter) {
        this.cPhoneLineParameter = cPhoneLineParameter;
    }

    /**
     * Sets the cLineParameter.
     *
     * @param cLineParameter the cLineParameter to be set.
     */
    public void setCLineParameter(Map<String, Object> cLineParameter) {
        this.cLineParameter = cLineParameter;
    }
}
