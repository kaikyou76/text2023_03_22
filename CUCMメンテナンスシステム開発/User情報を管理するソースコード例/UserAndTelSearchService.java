/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * UserAndTelSearchService.java
 * @date 2013/08/07
 * @version 1.0
 * @author KSC Yuichiro Yoshida
 */
package jp.co.netmarks.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.co.ksc.exception.ExcludeException;
import jp.co.ksc.service.BaseService;
import jp.co.ksc.util.LogHelpUtil;
import jp.co.netmarks.common.Constants;
import jp.co.netmarks.component.ReflectionManager;
import jp.co.netmarks.csv.Csvwriter;
import jp.co.netmarks.model.CUCMLineModel;
import jp.co.netmarks.model.LoginUserModel;
import jp.co.netmarks.model.UserAndTelSearchModel;
import jp.co.netmarks.model.UserAndTelUpdateModel;
import jp.co.netmarks.model.entity.ChargeAssociationEntity;
import jp.co.netmarks.model.entity.LineEntity;
import jp.co.netmarks.model.entity.PhoneEntity;
import jp.co.netmarks.model.entity.PhoneLineEntity;
import jp.co.netmarks.model.entity.TelDirAssociationEntity;
import jp.co.netmarks.model.entity.UnityAssociationEntity;
import jp.co.netmarks.model.entity.UserEntity;
import jp.co.netmarks.model.entity.UserPhoneEntity;
import jp.co.netmarks.model.entity.UserSectionEntity;
import jp.co.netmarks.model.entity.VoiceLoggerAssociationEntity;
import jp.co.netmarks.persistence.AppCommonMapper;
import jp.co.netmarks.persistence.ChargeAssociationMapper;
import jp.co.netmarks.persistence.LineMapper;
import jp.co.netmarks.persistence.PhoneLineMapper;
import jp.co.netmarks.persistence.PhoneMapper;
import jp.co.netmarks.persistence.TelDirAssociationMapper;
import jp.co.netmarks.persistence.UnityAssociationMapper;
import jp.co.netmarks.persistence.UserAndTelSearchMapper;
import jp.co.netmarks.persistence.UserMapper;
import jp.co.netmarks.persistence.UserPhoneMapper;
import jp.co.netmarks.persistence.UserSectionMapper;
import jp.co.netmarks.persistence.VoiceLoggerAssociationMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * <pre>
 * ユーザーと電話機の一覧用サービスクラス
 *
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/08/07 KSC Yuichiro Yoshida
 * </pre>
 * 
 * @author KSC Yuichiro Yoshida
 * @version 1.0 2013/08/07
 */
@Service
public class UserAndTelSearchService extends BaseService {
    private static Log log = LogFactory.getLog(UserAndTelSearchService.class);

    @Autowired
    private Properties properties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 新規共用電話化处理
     * 
     * @param params 共用電話パラメータ
     * @return メッセージ
     * @throws Exception
     */
    @Transactional
    public Map<String, String> sharedTelEdit(UserAndTelUpdateModel params) throws Exception {
        /* メッセージセット用 */
        Map<String, String> map = new HashMap<String, String>();
        try {
            // ユーザーIDをセット
            params.setUserId(userId);
            /* 共用電話作成处理 */
            userMapper.insertAppUser(setUserEntitySharedUse(params, timestamp));
            /* 成功メッセージをセット */
            map.put("sucsessMessage", properties.getProperty("update.success"));
        } catch (ExcludeException e) {
            /* 排他エラーメッセージをセット */
            map.put("errorMessage", properties.getProperty("exclusion.error"));
        }
        return map;
    }

    /**
     * 內線番号更新处理
     * 
     * @param userId    ユーザーID
     * @param timestamp タイムスタンプ
     */
    private void updateTelNumber(BigDecimal userId, Timestamp timestamp) {
        Map<String, Object> map = new HashMap<String, Object>();
        /* ユーザーに紐づく電話機の内線番号を取得 */
        map.put("userId", userId);
        map.put("COM_FLG_OFF", Constants.COM_FLG_OFF);
        String directoryNumber = appCommonMapper.getTelephoneNumber(map);
        /* ユーザーテーブルのTELEPHONENUMBERと更新フラグを更新 */
        userMapper.updateTelephoneNumber(setUserEntityTelephoneNumberInfo(userId, directoryNumber, timestamp));
    }

    /**
     * 共用電話名变更处理
     * 
     * @param params -
     * @return メッセージ
     * @throws Exception
     */
    @Transactional
    public Map<String, String> sharedTelUpdate(UserAndTelUpdateModel params) throws Exception {
        /* メッセージセット用 */
        Map<String, String> map = new HashMap<String, String>();
        /* パラメータをセット */
        UserEntity user = new UserEntity();
        user.setAppUserId(params.getUserId());
        user.setNameKanji(params.getSharedUserName());
        user.setLstupdtTmsTmp(timestamp);
        /* 共用名変更 */
        userMapper.updateUserKanjiName(user);
        /* 成功メッセージをセット */
        map.put("sucsessMessage", properties.getProperty("update.success"));
        return map;
    }

    /**
     * ユーザーと電話の紐付き削除処理
     * 
     * @param params    削除パラメータ
     * @param timestamp 14750J
     */
    private void userTelDelete(UserAndTelUpdateModel params, Timestamp timestamp) {
        /* 一般ユーザー、共有ユーザー別削除処理 */
        if (params.getSharedUse().equals(Constants.ENABLED_SHARED_USE_PRIVATE)) {
            /* 一般ユーザーの場合 */
        } else if (params.getSharedUse().equals(Constants.ENABLED_SHARED_USE_SHARE)) {
            /* 共有電話の場合 */
            /* ユーザー情報の物理削除 */
            UserEntity user = new UserEntity();
            user.setAppUserId(params.getUserId());
            userMapper.deleteAppUser(user);
        }
    }
}
