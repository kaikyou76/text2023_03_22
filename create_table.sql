/* 大会マスタ */
DROP TABLE IF EXISTS event_manage.m_event;
CREATE TABLE event_manage.m_event (
    event_id varchar(20) NOT NULL  comment '[大会ID]222' -- 大会ID
  , owner_id varchar(20) NOT NULL  comment '[主催者ID]' -- 主催者ID
  , event_name varchar(50) NOT NULL  comment '[大会名]' -- 大会名
  , holding_datetime datetime NOT NULL  comment '[開催日時]' -- 開催日時
  , holding_place varchar(100) NOT NULL  comment '[開催場所]' -- 開催場所
  , post_cd int(7) NOT NULL  comment '[郵便番号]' -- 郵便番号
  , prefectures varchar(100) NOT NULL  comment '[都道府県]' -- 都道府県
  , city varchar(100) NOT NULL  comment '[市区町村]' -- 市区町村
  , address varchar(100) NOT NULL  comment '[住所]' -- 住所
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (event_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[大会マスタ]大会情報のマスタ';

/* 主催者マスタ */
DROP TABLE IF EXISTS event_manage.m_owner;
CREATE TABLE event_manage.m_owner (
    owner_id varchar(20) NOT NULL  comment '[主催者ID]' -- 主催者ID
  , owner_name varchar(50) NOT NULL  comment '[主催者名]' -- 主催者名
  , ｒepresentative_name varchar(50) NOT NULL  comment '[代表者氏名]' -- 代表者氏名
  , post_cd int(7) NOT NULL  comment '[郵便番号]' -- 郵便番号
  , prefectures varchar(100) NOT NULL  comment '[都道府県]' -- 都道府県
  , city varchar(100) NOT NULL  comment '[市区町村]' -- 市区町村
  , address varchar(100) NOT NULL  comment '[住所]' -- 住所
  , call_number varchar(50) NOT NULL  comment '[電話番号]' -- 電話番号
  , mail_address varchar(100) NOT NULL  comment '[メールアドレス]' -- メールアドレス
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (owner_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[主催者マスタ]主催者情報のマスタ';

/* エントリ枠 */
DROP TABLE IF EXISTS event_manage.m_event_entry;
CREATE TABLE event_manage.m_event_entry (
    event_id varchar(20) NOT NULL  comment '[大会ID]222' -- 大会ID
  , entry_frame_id varchar(20) NOT NULL  comment '[エントリ枠ID]' -- エントリ枠ID
  , entry_frame_status varchar(1) NOT NULL  comment '[エントリ枠ステータス]1：募集前、2：募集中、3、抽選中、4：参加者確定' -- エントリ枠ステータス
  , event_type_id varchar(20) NOT NULL  comment '[種目ID]' -- 種目ID
  , lottery_div varchar(1) NOT NULL  comment '[抽選区分]1：先着順、2：抽選' -- 抽選区分
  , recruit_start_date date NOT NULL  comment '[募集開始年月日]' -- 募集開始年月日
  , recruit_end_date date NOT NULL  comment '[募集終了年月日]' -- 募集終了年月日
  , lottery_date date NOT NULL  comment '[抽選年月日]' -- 抽選年月日
  , entrant_limit int(7) NOT NULL  comment '[参加人数上限]' -- 参加人数上限
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (event_id,entry_frame_id)
, KEY m_event_entry_IX_01(entry_frame_status,lottery_div ,lottery_date)
, KEY m_event_entry_IX_02(entry_frame_status ,recruit_end_date ,recruit_start_date ,lottery_div)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[エントリ枠]大会ごとのエントリ枠のマスタ';

/* 大会サービス */
DROP TABLE IF EXISTS event_manage.m_event_service;
CREATE TABLE event_manage.m_event_service (
    event_id varchar(20) NOT NULL  comment '[大会ID]222' -- 大会ID
  , event_service_id varchar(20) NOT NULL  comment '[大会サービスID]' -- 大会サービスID
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (event_id,event_service_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[大会サービス]大会ごとのサービス情報のマスタ';

/* 大会アイテム */
DROP TABLE IF EXISTS event_manage.m_event_item;
CREATE TABLE event_manage.m_event_item (
    event_id varchar(20) NOT NULL  comment '[大会ID]222' -- 大会ID
  , event_item_id varchar(20) NOT NULL  comment '[大会アイテムID]' -- 大会アイテムID
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (event_id,event_item_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[大会アイテム]大会ごとのアイテムのマスタ';

/* 参加申込み */
DROP TABLE IF EXISTS event_manage.t_event_entry_user;
CREATE TABLE event_manage.t_event_entry_user (
    event_id varchar(20) NOT NULL  comment '[大会ID]222' -- 大会ID
  , user_id varchar(20) NOT NULL  comment '[会員ID]' -- 会員ID
  , entry_frame_id varchar(20) NOT NULL  comment '[エントリ枠ID]' -- エントリ枠ID
  , user_entry_status varchar(1) NOT NULL  comment '[会員申込み状態]1：申し込み済み、2：抽選中、3：参加確定(未払い)、4：参加確定(支払済)、5：落選、9：キャンセル' -- 会員申込み状態
  , payment_date date   comment '[支払い年月日]' -- 支払い年月日
  , payment_amount int(7)   comment '[支払金額]' -- 支払金額
  , payment_confirm_date date   comment '[支払確認年月日]' -- 支払確認年月日
  , point_granted_flg varchar(1) NOT NULL  comment '[ポイント付与有無]0：ポイント計算前、1：ポイント計算済み' -- ポイント付与有無
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (event_id,user_id)
, KEY t_event_entry_user_IX_01(point_granted_flg,payment_date)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[参加申込み]会員が申し込んだ情報を保持するテーブル';

/* 会員マスタ */
DROP TABLE IF EXISTS event_manage.m_user;
CREATE TABLE event_manage.m_user (
    user_id varchar(20) NOT NULL  comment '[会員ID]' -- 会員ID
  , user_name varchar(50) NOT NULL  comment '[会員名]' -- 会員名
  , gender varchar(1) NOT NULL  comment '[性別]0：不明、1：男性、2：女性、9：適用不能' -- 性別
  , birthday date   comment '[生年月日]' -- 生年月日
  , mail_address varchar(100) NOT NULL  comment '[メールアドレス]' -- メールアドレス
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[会員マスタ]会員のマスタ';

/* 会員ポイント */
DROP TABLE IF EXISTS event_manage.t_user_point;
CREATE TABLE event_manage.t_user_point (
    user_id varchar(20) NOT NULL  comment '[会員ID]' -- 会員ID
  , point int(7) NOT NULL  comment '[ポイント残高]' -- ポイント残高
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[会員ポイント]会員のポイント情報を保持するテーブル';

/* パラメータマスタ */
DROP TABLE IF EXISTS event_manage.m_param;
CREATE TABLE event_manage.m_param (
    param_id varchar(20) NOT NULL  comment '[パラメータID]' -- パラメータID
  , param_val varchar(100) NOT NULL  comment '[パラメータ値]' -- パラメータ値
  , delete_flg varchar(1) NOT NULL  comment '[削除フラグ]0：有効、1：論理削除' -- 削除フラグ
  , create_user_id varchar(20) NOT NULL  comment '[作成者]' -- 作成者
  , create_datetime datetime NOT NULL  comment '[作成日時]' -- 作成日時
  , update_user_id varchar(20) NOT NULL  comment '[更新者]' -- 更新者
  , update_datetime datetime NOT NULL  comment '[更新日時]' -- 更新日時
  , PRIMARY KEY (param_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[パラメータマスタ]パラメータを管理するテーブル';


