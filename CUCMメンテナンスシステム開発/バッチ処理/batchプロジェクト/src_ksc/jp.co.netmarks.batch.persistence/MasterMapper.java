/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * MasterMapper.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.netmarks.batch.persistence;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Master パラメータ取得マッパークラス
 *
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/09/12 KSC Hiroaki Endo
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public interface MasterMapper {

    /**
     * フルVacuumを実行
     * @return int
     */
    int fullvacuum();
 
    /**
     * 日次用 VacuumとAnalyze を実行
     * @return int
     */
    int dayvacuum();

    /**
     * マスターパラメータ関連テーブルのVacuum を実行
     * @return int
     */
    int vacuum();

    /**
     * マスターパラメータ関連テーブルのレコード削除
     * @param parameterValues
     * @return 削除件数
     */
    int deleteMaster(Map<String, Object> parameterValues);

    /**
     * CallingSearchSpaceの登録
     * @param cssParameter
     * @return 件数
     */
    int insertCallingSearchSpace(Map<String, Object> cssParameter);

    /**
     * DevicePoolの登録
     * @param devicePoolParameter
     * @return 件数
     */
    int insertDevicePool(Map<String, Object> devicePoolParameter);

    /**
     * Locationの登録
     * @param locationParameter
     * @return 件数
     */
    int insertLocation(Map<String, Object> locationParameter);

    /**
     * phoneTemplateの登録
     * @param phoneTempParameter
     * @return 件数
     */
    int insertPhoneTemplate(Map<String, Object> phoneTempParameter);

    /**
     * TypeModelの登録
     * @param typeModelParameter
     * @return 件数
     */
    int insertTypeModel(Map<String, Object> typeModelParameter);

    /**
     * マスターパラメータの取得
     * @param parameterValues
     * @return 取得したマスターパラメータのList
     */
    List<Map<String, Object>> selectMaster(Map<String, Object> parameterValues);
}
