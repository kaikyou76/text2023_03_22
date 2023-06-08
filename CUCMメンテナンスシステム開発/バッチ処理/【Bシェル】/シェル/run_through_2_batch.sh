#!/bin/sh
###################################################################################
# 機能概要 夜間バッチ後半 JOB起動シェル
#
# 引数　　:無
# 
# 戻り値 :==0 正常終了
#　　　　　　!=0 異常終了
####################################################################################
export TMP_DIR=/home/batchuser/tmp 
export SCRIPT_DIR=/home/batchuser/job 
export MSG_DATE=$(date +%Y-%m-%d)
export LOG_DATE=$(date +"%Y/%m/%d %p %I:%M:%S")
export LOG=/var/www/download/logs/batch/after_batch_executed_result_$MSG_DATE.log
#------監視システム用------#
export EXECUTIVE_START=START
export EXECUTIVE_OK=OK
export EXECUTIVE_NG=NG
#人事情報及びAD情報取込(退職者情報出力)
export EXECUTIVE_LOG_EMP=EmployeeADImport 
#通録サーバ情報 CSVインポート
export EXECUTIVE_LOG_VOICE=VoiceLoggerImport 
#CUCMマスタパラメータ/双方向チェックデータの取得
export EXECUTIVE_LOG_GETCUCM=GetCUCMData 
#バッチ処理後のビジネスフォンクラスタ反映
export EXECUTIVE_LOG_CUCMREFLECT=SecondCUCMReflect 
#システム再開・ログイン画面への復旧
export EXECUTIVE_LOG_LOGIN=ChangeLogin

echo "$LOG_DATE: ※バッチ処理後半開始。 人事情報取り込み処理以降を実行します。" >> $LOG
#------------#
# JOBOTの実行 　#
#------------#
if [ ! -e $TMP_DIR/run_turough_1_END ]; then
    echo "$LOG_DATE: ※夜間パッチ処理" >> $LOG
    echo "$LOG_DATE: 前半処理に異常有りの為、後半処理の実行を中止します。" >> $LOG 
    exit 9
fi

if [ -e $TMP_DIR/run_turough_2_END ]; then
    echo "$LOG_DATE: ※夜間パッチ処理 後半正常終了。" >> $LOG
    exit 0
fi

export SHRESULT=0
#--------------------------------------
#人事情報取り込み・退職者情報出力
#--------------------------------------
#監視ファイル作成
/usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_EMP $EXECUTIVE_START

#EOFAM EOFADファイルの作成
touch /home/batchuser/files/importfiles/EOFAM 
touch /home/batchuser/files/importfiles/EOFAD
$SCRIPT_DIR/LoadPersonnelInfo.sh
JINJI=$?
if [ $JINJI -eq 0 ]; then
    #-----------------------------------
    #CSVインポート
    #-----------------------------------
    #監視正常終了ファイル作成
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_EMP $EXECUTIVE_OK 
    #監視ファイル作成
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_VOICE $EXECUTIVE_START
    $SCRIPT_DIR/csvVoiceLoggerImport.sh
    SHRESULT=$?
    #--------------------------------------
    #マスタパラメータ取得・取込
    #--------------------------------------
    if [ $SHRESULT -eq 0 ]; then
        #監視正常終了ファイル作成
        /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_VOICE $EXECUTIVE_OK 
        #監視ファイル作成
        /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_GETCUCM $EXECUTIVE_START 
        $SCRIPT_DIR/getMasterParameter.sh
        SHRESULT=$?
    else
        echo "$LOG_DATE: バッチ処理後半[CSVインポート処理]にてエラー" >> $LOG
        echo "$LOG_DATE: [osvVoiceLoggerImport.sh]" >> $LOG
        $SCRIPT_DIR/set_error.sh
        #-------------------------#
        # 監視システムへエラーファイル転送 #
        #-------------------------#
        /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_VOICE $EXECUTIVE_NG
        exit 9
    fi
    #----------------------------------------
    #双方向チェックデータ取得・取込
    #----------------------------------------
    if [ $SHRESULT -eq 0 ]; then 
        $SCRIPT_DIR/getConsistencyInfo.sh
        SHRESULT=$?
    else
        echo "$LOG_DATE: バッチ処理後半[マスタパラメータ取得・取込処理]にてエラー" >> $LOG 
        echo "$LOG_DATE: [getMasterParameter.sh]" >> $LOG
        $SCRIPT_DIR/set_error.sh

        #------------------------------
        # 監視システムへエラーファイル転送 #
        #------------------------------
        /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_GETCUCM $EXECUTIVE_NG
        exit 9
    fi
    #-----------------------------------
    # CUCM反映 + 更新依頼フラグ変更
    #-----------------------------------
    if [ $SHRESULT -eq 0 ]; then
        #監視正常終了ファイル作成
        /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_GETCUCM $EXECUTIVE_OK 
        #監視ファイル作成
        /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_CUCMREFLECT $EXECUTIVE_START 
        $SCRIPT_DIR/DailyCUCMReflection.sh
        SHRESULT=$?
    else
        echo "$LOG_DATE: バッチ処理後半[双方向チェックデータ取得・取込]にてエラー" >> $LOG 
        echo "$LOG_DATE: [getConsistencyInfo.sh]" >> $LOG
        $SCRIPT_DIR/set_error.sh
        #----------------------#
        # 監視システムへエラーファイル転送 #
        #----------------------#
        /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_GETCUCM $EXECUTIVE_NG
        exit 9
    fi
    #-----------------------------------------------------------
else
    echo "$LOG_DATE: バッチ処理後半[人事情報取り込み・退職者情報出力処理]にてエラー" >> $LOG 
    echo "$LOG_DATE: [LoadPersonnel Info.sh]" >> $LOG
    $SCRIPT_DIR/set_error.sh
    #-----------------------#
    # 監視システムへエラーファイル転送 #
    #-----------------------#
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_EMP $EXECUTIVE_NG
    exit 9
fi
#-------------------------
#ログイン画面設定処理
#-------------------------
if [ $SHRESULT -eq 0 ]; then
    #監視正常終了ファイル作成
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_CUCMREFLECT $EXECUTIVE_OK 
    #監視ファイル作成
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_LOGIN $EXECUTIVE_START 
    $SCRIPT_DIR/set_login.sh
    SHRESULT=$?
else
    echo "$LOG_DATE: バッチ処理後半[CUCM反映処理]にてエラー" >> $LOG
    echo "$LOG_DATE: [DailyCUCMReflection.sh]" >> $LOG
    $SCRIPT_DIR/set_error.sh
    #-----------------------#
    # 監視システムへエラーファイル転送 #
    #-----------------------#
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_CUCMREFLECT $EXECUTIVE_NG
    exit 9
fi
#----------------------------------------------------------------
if [ $SHRESULT -eq 0 ]; then
    #監視正常終了ファイル作成
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_LOGIN $EXECUTIVE_OK 
    echo "$LOG_DATE: バッチ処理後半終了。" >> $LOG
    touch $TMP_DIR/run_turough_2_END
    if [ -e $TMP_DIR/run_turough_1_END ]; then
        rm -f $TMP_DIR/run_turough_1_END
    fi
else
    echo "$LOG_DATE: バッチ処理後半 [ログイン画面設定処理]にてエラー" >> $LOG 
    echo "$LOG_DATE: [set_login.sh]" >> $LOG
    $SCRIPT_DIR/set_error.sh
    #---------------------#
    #監視システムへエラーファイル転送 #
    #---------------------#
    /usr/local/shell/sendstatus.sh $EXECUTIVE_LOG_LOGIN $EXECUTIVE_NG
    exit 9
fi
#--------------------------------------------------------------------
exit 0
