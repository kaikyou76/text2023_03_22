/*
 * MasterWriter.java
 *
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
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
import jp.co.ksc.batch.logic.MasterUpdManager;
import jp.co.ksc.batch.util.BatchSettings;
import jp.co.ksc.batch.util.LockFileManager;
import jp.co.netmarks.batch.dao.VacuumDAO;
import jp.co.netmarks.batch.persistence.MasterMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <pre>
 * マスタパラメータ取得 Writer
 * </pre>
 * 
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/09/12 KSC Hiroaki Endo
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class MasterWriter implements ItemWriter<Object> {
    private static final Log log = LogFactory.getLog(MasterWriter.class);

    private MasterUpdManager manager = new MasterUpdManager();

    @Autowired
    private Properties properties;

    @Autowired
    private MasterMapper masMapper;

    @Autowired
    private VacuumDAO vd;

    // 各テーブル更新用パラメータ
    private Map<String, Object> cssParameter = new HashMap<String, Object>();
    private Map<String, Object> devicePoolParameter = new HashMap<String, Object>();
    private Map<String, Object> locationParameter = new HashMap<String, Object>();
    private Map<String, Object> pickupParameter = new HashMap<String, Object>();
    private Map<String, Object> phoneTempParameter = new HashMap<String, Object>();
    private Map<String, Object> typeModelParameter = new HashMap<String, Object>();
    private Map<String, Object> parameterValues = new HashMap<String, Object>();

    /**
     * Master パラメータ取得処理メイン
     * 
     * @param paramList 5-7
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
            log.info("マスタパラメータ取得開始");

            // 取得前に各テーブルをクリア
            masMapper.deleteMaster(parameterValues);

            log.info("マスタパラメータ取得クラスタ情報の取得");
            // クラスタ情報の取得
            List<Map<String, Object>> hostList = masMapper.selectMaster(parameterValues);

            log.info("マスタパラメータ取得 取得してList化");

            // CUCMから各マスタパラメータを取得してList化
            String typeInStr = properties.getProperty("type.model.name");
            String[] typeModel = typeInStr.split(",", 0);
            typeInStr = "";

            for (int i = 0; i < typeModel.length; i++) {
                if (i != 0)
                    typeInStr += ".";
                typeInStr += typeModel[i];
            }

            List<List<Map<String, Object>>> param = manager.start(hostList, typeInStr);

            // クラスタ毎に取得したテーブル値を加工し、Insert
            int cnt = 0;
            for (int id = 0; id < hostList.size(); id++) {
                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setCssParameter(param.get(cnt).get(i));
                    masMapper.insertCallingSearchSpace(cssParameter);
                }

                cnt++;

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setDevicePoolParameter(param.get(cnt).get(i));
                    masMapper.insertDevicePool(devicePoolParameter);
                }

                cnt++;

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setLocationParameter(param.get(cnt).get(i));
                    masMapper.insertLocation(locationParameter);
                }

                cnt++;

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setPhoneTempParameter(param.get(cnt).get(i));
                    masMapper.insertPhoneTemplate(phoneTempParameter);
                }

                cnt++;

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setPickupParameter(param.get(cnt).get(i));
                    masMapper.insertPickupGroup(pickupParameter);
                }

                cnt++;

                for (int i = 0; i < param.get(cnt).size(); i++) {
                    setTypeModelParameter(param.get(cnt).get(i));
                    masMapper.insertTypeModel(typeModelParameter);
                }
            }

            // vacuum
            log.info("MasterParameter VacuumAnalyze 6");
            vd.masterVacuum();
            log.info("MasterParameter VacuumAnalyze 7");

            log.info("マスタパラメータ取得終了");
        } catch (Exception e) {
            throw e;
        } finally {
            // ロック解除
            LockFileManager.unlock(bs);
        }
    }

    /**
     * parameterValuesを設定します。
     * 
     * @param parameterValues parameterValues
     */
    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * cssParameterを設定します。
     * 
     * @param cssParameter cssParameter
     */
    public void setCssParameter(Map<String, Object> cssParameter) {
        this.cssParameter = cssParameter;
    }

    /**
     * devicePoolParameterを設定します。
     * 
     * @param devicePoolParameter devicePoolParameter
     */
    public void setDevicePoolParameter(Map<String, Object> devicePoolParameter) {
        this.devicePoolParameter = devicePoolParameter;
    }

    /**
     * locationParameterを設定します。
     * 
     * @param locationParameter locationParameter
     */
    public void setLocationParameter(Map<String, Object> locationParameter) {
        this.locationParameter = locationParameter;
    }

    /**
     * pickupParameterを設定します。
     * 
     * @param pickupParameter pickupParameter
     */
    public void setPickupParameter(Map<String, Object> pickupParameter) {
        this.pickupParameter = pickupParameter;
    }

    /**
     * phoneTempParameterを設定します。
     * 
     * @param phoneTempParameter phoneTempParameter
     */
    public void setPhoneTempParameter(Map<String, Object> phoneTempParameter) {
        this.phoneTempParameter = phoneTempParameter;
    }

    /**
     * typeModelParameterを設定します。
     * 
     * @param typeModelParameter typeModelParameter
     */
    public void setTypeModelParameter(Map<String, Object> typeModelParameter) {
        this.typeModelParameter = typeModelParameter;
    }
}
