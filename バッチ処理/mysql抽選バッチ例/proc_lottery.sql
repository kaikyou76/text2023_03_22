DROP PROCEDURE IF EXISTS event_manage.proc_lottery;

-- 区切り文字の変更
DELIMITER //
create procedure event_manage.proc_lottery()
begin
    --  カーソル内容を格納する用の変数
    DECLARE vwk_event_id       VARCHAR(100);
    DECLARE vwk_entry_frame_id VARCHAR(100);
    DECLARE nwk_entrant_limit  int;
    DECLARE vwk_user_id        VARCHAR(100);
    DECLARE nwk_rand_num       float;
    
    -- ループ制御用変数
    DECLARE nwk_entrant_count        int;
    DECLARE vwk_last_event_id        VARCHAR(100);
    DECLARE vwk_last_entry_frame_id  VARCHAR(100);
    DECLARE vwk_lottery_result       VARCHAR(100);
 
    -- 「DECLARE done INT DEFAULT 0;」は、ストアドプロシージャ内で使用されるローカル変数の宣言と初期化を行っています。
    --  ここでは、「done」という整数型の変数を宣言し、初期値として0を設定しています。
    DECLARE done INT DEFAULT 0;
    -- カーソルの定義
    DECLARE cur1 CURSOR FOR 
    SELECT * FROM(
                  SELECT t.event_id
                        ,t.entry_frame_id
                        ,t.entrant_limit
                        ,s.user_id
                        ,RAND() as rand_num
                   FROM event_manage.m_event_entry t
                  INNER JOIN event_manage.t_event_entry_user s on t.event_id = s.event_id and t.entry_frame_id = s.entry_frame_id
                  WHERE t.lottery_date <= sysdate
                    and t.lottery_div = '2'
                    and t.entry_frame_status <> '4'
                 )v order by event_id, entry_frame_id, rand_num
    ;
    -- エラーハンドラの宣言を行っています。
    -- ここでは、SQLSTATEが '02000' であるエラーが発生した場合、変数「done」に1を設定するハンドラを設定しています。
    -- このようなハンドラを設定することで、エラーが発生してもプロシージャが正常に続行されるようになります。
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1:-- 最終レコードに到達した際の処理(doneの値を1に変更する)
    
    -- 抽選処理
    -- 制御用の変数の初期化
    set nwk_entrant_count = 1;      
    set vwk_last_event_id = '';     
    set vwk_last_entry_frame_id = '';
    
    -- カーソルを開けていきます。
    OPEN cur1;
    -- 「fetch」はカーソルの各行を読み込んで、各値を変数に入れ込みます。
    fetch cur1 into vwk_event_id, vwk_entry_frame_id, nwk_entrant_limit, vwk_user_id, nwk_rand_num;
    while done != 1 do
      -- 大会ID, エントリ枠IDが異なる場合、カウンタを初期化してエントリ枠の状態を変更する
      IF vwk_last_event_id <> vwk_event_id or vwk_last_entry_frame_id <> vwk_entry_frame_id then
        set nwk_entrant_count = 1;
        UPDATE event_manage.m_event_entry t
           SET t.entry_frame_status = '4'
         WHERE t.event_id = vwk_last_event_id
           AND t.entry_frame_id = vwk_last_entry_frame_id
        ;
      END IF;
      
      -- 抽選結果の設定
      IF nwk_entrant_limit >= nwk_entrant_count THEN
         -- 当選の場合
         set vwk_lottery_result = '3':
      ELSE
         -- 当選なし場合
         set vwk_lottery_result = '5':
      END IF;
      
      -- 抽選結果で更新
      UPDATE event_manage.t_event_entry_user s
         SET s.user_entry_status = vwk_lottery_result
       WHERE s.event_id = vwk_event_id
         AND s.entry_frame_id = vwk_entry_frame_id       
         AND s.user_id = vwk_user_id
      ; 
      
      -- 次の行の準備
      set nwk_entrant_count = nwk_entrant_count + 1;      
      set vwk_last_event_id = vwk_event_id;     
      set vwk_last_entry_frame_id = vwk_entry_frame_id; 
      
      fetch cur1 into vwk_event_id, vwk_entry_frame_id, nwk_entrant_limit, vwk_user_id, nwk_rand_num;           
    end while;
    
end
//
-- 区切り文字をもとに戻す
DELIMITER
