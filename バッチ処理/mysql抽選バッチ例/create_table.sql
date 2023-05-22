/* ���}�X�^ */
DROP TABLE IF EXISTS event_manage.m_event;
CREATE TABLE event_manage.m_event (
    event_id varchar(20) NOT NULL  comment '[���ID]222' -- ���ID
  , owner_id varchar(20) NOT NULL  comment '[��Î�ID]' -- ��Î�ID
  , event_name varchar(50) NOT NULL  comment '[��]' -- ��
  , holding_datetime datetime NOT NULL  comment '[�J�Ó���]' -- �J�Ó���
  , holding_place varchar(100) NOT NULL  comment '[�J�Ïꏊ]' -- �J�Ïꏊ
  , post_cd int(7) NOT NULL  comment '[�X�֔ԍ�]' -- �X�֔ԍ�
  , prefectures varchar(100) NOT NULL  comment '[�s���{��]' -- �s���{��
  , city varchar(100) NOT NULL  comment '[�s�撬��]' -- �s�撬��
  , address varchar(100) NOT NULL  comment '[�Z��]' -- �Z��
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (event_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[���}�X�^]�����̃}�X�^';

/* ��Î҃}�X�^ */
DROP TABLE IF EXISTS event_manage.m_owner;
CREATE TABLE event_manage.m_owner (
    owner_id varchar(20) NOT NULL  comment '[��Î�ID]' -- ��Î�ID
  , owner_name varchar(50) NOT NULL  comment '[��ÎҖ�]' -- ��ÎҖ�
  , ��epresentative_name varchar(50) NOT NULL  comment '[��\�Ҏ���]' -- ��\�Ҏ���
  , post_cd int(7) NOT NULL  comment '[�X�֔ԍ�]' -- �X�֔ԍ�
  , prefectures varchar(100) NOT NULL  comment '[�s���{��]' -- �s���{��
  , city varchar(100) NOT NULL  comment '[�s�撬��]' -- �s�撬��
  , address varchar(100) NOT NULL  comment '[�Z��]' -- �Z��
  , call_number varchar(50) NOT NULL  comment '[�d�b�ԍ�]' -- �d�b�ԍ�
  , mail_address varchar(100) NOT NULL  comment '[���[���A�h���X]' -- ���[���A�h���X
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (owner_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[��Î҃}�X�^]��Îҏ��̃}�X�^';

/* �G���g���g */
DROP TABLE IF EXISTS event_manage.m_event_entry;
CREATE TABLE event_manage.m_event_entry (
    event_id varchar(20) NOT NULL  comment '[���ID]222' -- ���ID
  , entry_frame_id varchar(20) NOT NULL  comment '[�G���g���gID]' -- �G���g���gID
  , entry_frame_status varchar(1) NOT NULL  comment '[�G���g���g�X�e�[�^�X]1�F��W�O�A2�F��W���A3�A���I���A4�F�Q���Ҋm��' -- �G���g���g�X�e�[�^�X
  , event_type_id varchar(20) NOT NULL  comment '[���ID]' -- ���ID
  , lottery_div varchar(1) NOT NULL  comment '[���I�敪]1�F�撅���A2�F���I' -- ���I�敪
  , recruit_start_date date NOT NULL  comment '[��W�J�n�N����]' -- ��W�J�n�N����
  , recruit_end_date date NOT NULL  comment '[��W�I���N����]' -- ��W�I���N����
  , lottery_date date NOT NULL  comment '[���I�N����]' -- ���I�N����
  , entrant_limit int(7) NOT NULL  comment '[�Q���l�����]' -- �Q���l�����
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (event_id,entry_frame_id)
, KEY m_event_entry_IX_01(entry_frame_status,lottery_div ,lottery_date)
, KEY m_event_entry_IX_02(entry_frame_status ,recruit_end_date ,recruit_start_date ,lottery_div)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[�G���g���g]���Ƃ̃G���g���g�̃}�X�^';

/* ���T�[�r�X */
DROP TABLE IF EXISTS event_manage.m_event_service;
CREATE TABLE event_manage.m_event_service (
    event_id varchar(20) NOT NULL  comment '[���ID]222' -- ���ID
  , event_service_id varchar(20) NOT NULL  comment '[���T�[�r�XID]' -- ���T�[�r�XID
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (event_id,event_service_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[���T�[�r�X]���Ƃ̃T�[�r�X���̃}�X�^';

/* ���A�C�e�� */
DROP TABLE IF EXISTS event_manage.m_event_item;
CREATE TABLE event_manage.m_event_item (
    event_id varchar(20) NOT NULL  comment '[���ID]222' -- ���ID
  , event_item_id varchar(20) NOT NULL  comment '[���A�C�e��ID]' -- ���A�C�e��ID
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (event_id,event_item_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[���A�C�e��]���Ƃ̃A�C�e���̃}�X�^';

/* �Q���\���� */
DROP TABLE IF EXISTS event_manage.t_event_entry_user;
CREATE TABLE event_manage.t_event_entry_user (
    event_id varchar(20) NOT NULL  comment '[���ID]222' -- ���ID
  , user_id varchar(20) NOT NULL  comment '[���ID]' -- ���ID
  , entry_frame_id varchar(20) NOT NULL  comment '[�G���g���gID]' -- �G���g���gID
  , user_entry_status varchar(1) NOT NULL  comment '[����\���ݏ��]1�F�\�����ݍς݁A2�F���I���A3�F�Q���m��(������)�A4�F�Q���m��(�x����)�A5�F���I�A9�F�L�����Z��' -- ����\���ݏ��
  , payment_date date   comment '[�x�����N����]' -- �x�����N����
  , payment_amount int(7)   comment '[�x�����z]' -- �x�����z
  , payment_confirm_date date   comment '[�x���m�F�N����]' -- �x���m�F�N����
  , point_granted_flg varchar(1) NOT NULL  comment '[�|�C���g�t�^�L��]0�F�|�C���g�v�Z�O�A1�F�|�C���g�v�Z�ς�' -- �|�C���g�t�^�L��
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (event_id,user_id)
, KEY t_event_entry_user_IX_01(point_granted_flg,payment_date)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[�Q���\����]������\�����񂾏���ێ�����e�[�u��';

/* ����}�X�^ */
DROP TABLE IF EXISTS event_manage.m_user;
CREATE TABLE event_manage.m_user (
    user_id varchar(20) NOT NULL  comment '[���ID]' -- ���ID
  , user_name varchar(50) NOT NULL  comment '[�����]' -- �����
  , gender varchar(1) NOT NULL  comment '[����]0�F�s���A1�F�j���A2�F�����A9�F�K�p�s�\' -- ����
  , birthday date   comment '[���N����]' -- ���N����
  , mail_address varchar(100) NOT NULL  comment '[���[���A�h���X]' -- ���[���A�h���X
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[����}�X�^]����̃}�X�^';

/* ����|�C���g */
DROP TABLE IF EXISTS event_manage.t_user_point;
CREATE TABLE event_manage.t_user_point (
    user_id varchar(20) NOT NULL  comment '[���ID]' -- ���ID
  , point int(7) NOT NULL  comment '[�|�C���g�c��]' -- �|�C���g�c��
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[����|�C���g]����̃|�C���g����ێ�����e�[�u��';

/* �p�����[�^�}�X�^ */
DROP TABLE IF EXISTS event_manage.m_param;
CREATE TABLE event_manage.m_param (
    param_id varchar(20) NOT NULL  comment '[�p�����[�^ID]' -- �p�����[�^ID
  , param_val varchar(100) NOT NULL  comment '[�p�����[�^�l]' -- �p�����[�^�l
  , delete_flg varchar(1) NOT NULL  comment '[�폜�t���O]0�F�L���A1�F�_���폜' -- �폜�t���O
  , create_user_id varchar(20) NOT NULL  comment '[�쐬��]' -- �쐬��
  , create_datetime datetime NOT NULL  comment '[�쐬����]' -- �쐬����
  , update_user_id varchar(20) NOT NULL  comment '[�X�V��]' -- �X�V��
  , update_datetime datetime NOT NULL  comment '[�X�V����]' -- �X�V����
  , PRIMARY KEY (param_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 comment = '[�p�����[�^�}�X�^]�p�����[�^���Ǘ�����e�[�u��';


