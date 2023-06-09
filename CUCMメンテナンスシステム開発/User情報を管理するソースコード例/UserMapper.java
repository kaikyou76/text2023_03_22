/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * UserMapper.java
 * @date 2013/08/22
 * @version 1.0
 * @author KSC Yuichiro Yoshida
 */
package jp.co.netmarks.persistence;

import jp.co.netmarks.model.entity.UserEntity;

/**
 * Userテーブル用データマッパーインタフェース
 * < MODIFICATION HISTORY>
 * 1.0 2013/08/22 KSC Yuichiro Yoshida
 */
public interface UserMapper {
    /**
     * ユーザーテーブル登録
     * @param params 登録パラメータ
     * @return
     */
    int insertAppUser(UserEntity params);

    /**
     * ユーザーテーブルの電話番号を更新する
     * @param params 更新パラメータ
     * @return
     */
    int updateTelephoneNumber(UserEntity params);

    /**
     * ユーザーテーブルのログインパスワードを更新する
     * @param params 更新パラメータ
     * @return 更新件数
     */
    int updateLoginPassword(UserEntity params);

    /**
     * ユーザーテーブルの権限を更新する
     * @param params 更新パラメータ
     * @return
     */
    int updateUserRole(UserEntity params);

    /**
     * ユーザー名を更新する
     * @param params 更新パラメータ
     * @return
     */
    int updateUserKanjiName(UserEntity params);

    /**
     * ユーザーテーブル削除
     * @param params
     * @return
     */
    int deleteAppUser(UserEntity params);
}
