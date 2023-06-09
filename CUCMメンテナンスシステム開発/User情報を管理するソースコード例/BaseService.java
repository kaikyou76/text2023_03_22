/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * BaseService.java
 * @date 2013/08/27
 * @version 1.0
 * @author KSC Tomomichi Iwasawa
 */
package jp.co.ksc.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import jp.co.ksc.exception.ExcludeException;
import jp.co.ksc.persistence.CommonMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <pre>
 * ★ 基底サービスクラス
 * &lt; MODIFICATION HISTORY&gt;
 * 1.0 2013/08/01 KSC Tomomichi Iwasawa
 * </pre>
 * 
 * @author KSC Tomomichi Iwasawa
 * @version 1.0 2013/08/01
 */
public class BaseService {
    @SuppressWarnings("unused")
    private static Log log;

    @Autowired
    LogFactory.getLog(BaseService.class);

    @Autowired
    private CommonMapper commonMapper;

    /**
     * ★ 排他ロックを取得する
     * ※ロックが取得できない場合は、ExcludeExceptionを返す
     * 
     * @param tableName     テーブル名
     * @param lstupdtTmstmp 最終更新タイムスタンプ
     * @param primaryKeys   プライマリキー（[0]=カラム名, [1]=値, [2]=カラム名2, [3]=値2）
     * @return true:ロック取得成功
     * @throws Exception
     */
    protected boolean locked(String tableName, Timestamp lstupdtTmstmp, Map<String, Object> primaryKeys)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableName", tableName);
        map.put("lstupdtTmstmp", lstupdtTmstmp);
        map.put("primaryKeys", primaryKeys.entrySet());

        /* 排他エラー */
        if (commonMapper.locked(map) == null) {
            throw new ExcludeException(map);
        }
        return true;
    }

    /**
     * シーケンスを取得する
     * 
     * @param sequenceName シーケンス名
     * @return シーケンス値
     */
    protected String sequence(String sequenceName) {
        return commonMapper.sequence(sequenceName);
    }
}
