DROP PROCEDURE IF EXISTS event_manage.proc_lottery;

-- ��؂蕶���̕ύX
DELIMITER //
create procedure event_manage.proc_lottery()
begin
    --  �J�[�\�����e���i�[����p�̕ϐ�
    DECLARE vwk_event_id       VARCHAR(100);
    DECLARE vwk_entry_frame_id VARCHAR(100);
    DECLARE nwk_entrant_limit  int;
    DECLARE vwk_user_id        VARCHAR(100);
    DECLARE nwk_rand_num       float;
    
    -- ���[�v����p�ϐ�
    DECLARE nwk_entrant_count        int;
    DECLARE vwk_last_event_id        VARCHAR(100);
    DECLARE vwk_last_entry_frame_id  VARCHAR(100);
    DECLARE vwk_lottery_result       VARCHAR(100);
 
    -- �uDECLARE done INT DEFAULT 0;�v�́A�X�g�A�h�v���V�[�W�����Ŏg�p����郍�[�J���ϐ��̐錾�Ə��������s���Ă��܂��B
    --  �����ł́A�udone�v�Ƃ��������^�̕ϐ���錾���A�����l�Ƃ���0��ݒ肵�Ă��܂��B
    DECLARE done INT DEFAULT 0;
    -- �J�[�\���̒�`
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
    -- �G���[�n���h���̐錾���s���Ă��܂��B
    -- �����ł́ASQLSTATE�� '02000' �ł���G���[�����������ꍇ�A�ϐ��udone�v��1��ݒ肷��n���h����ݒ肵�Ă��܂��B
    -- ���̂悤�ȃn���h����ݒ肷�邱�ƂŁA�G���[���������Ă��v���V�[�W��������ɑ��s�����悤�ɂȂ�܂��B
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1:-- �ŏI���R�[�h�ɓ��B�����ۂ̏���(done�̒l��1�ɕύX����)
    
    -- ���I����
    -- ����p�̕ϐ��̏�����
    set nwk_entrant_count = 1;      
    set vwk_last_event_id = '';     
    set vwk_last_entry_frame_id = '';
    
    -- �J�[�\�����J���Ă����܂��B
    OPEN cur1;
    -- �ufetch�v�̓J�[�\���̊e�s��ǂݍ���ŁA�e�l��ϐ��ɓ��ꍞ�݂܂��B
    fetch cur1 into vwk_event_id, vwk_entry_frame_id, nwk_entrant_limit, vwk_user_id, nwk_rand_num;
    while done != 1 do
      -- ���ID, �G���g���gID���قȂ�ꍇ�A�J�E���^�����������ăG���g���g�̏�Ԃ�ύX����
      IF vwk_last_event_id <> vwk_event_id or vwk_last_entry_frame_id <> vwk_entry_frame_id then
        set nwk_entrant_count = 1;
        UPDATE event_manage.m_event_entry t
           SET t.entry_frame_status = '4'
         WHERE t.event_id = vwk_last_event_id
           AND t.entry_frame_id = vwk_last_entry_frame_id
        ;
      END IF;
      
      -- ���I���ʂ̐ݒ�
      IF nwk_entrant_limit >= nwk_entrant_count THEN
         -- ���I�̏ꍇ
         set vwk_lottery_result = '3':
      ELSE
         -- ���I�Ȃ��ꍇ
         set vwk_lottery_result = '5':
      END IF;
      
      -- ���I���ʂōX�V
      UPDATE event_manage.t_event_entry_user s
         SET s.user_entry_status = vwk_lottery_result
       WHERE s.event_id = vwk_event_id
         AND s.entry_frame_id = vwk_entry_frame_id       
         AND s.user_id = vwk_user_id
      ; 
      
      -- ���̍s�̏���
      set nwk_entrant_count = nwk_entrant_count + 1;      
      set vwk_last_event_id = vwk_event_id;     
      set vwk_last_entry_frame_id = vwk_entry_frame_id; 
      
      fetch cur1 into vwk_event_id, vwk_entry_frame_id, nwk_entrant_limit, vwk_user_id, nwk_rand_num;           
    end while;
    
end
//
-- ��؂蕶�������Ƃɖ߂�
DELIMITER
