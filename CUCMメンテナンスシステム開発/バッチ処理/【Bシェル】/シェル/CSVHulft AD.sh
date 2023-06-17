#!/bin/sh

###################################################
# 機能概要: OUTPUT CSV Hulft 転送テスト
#
# 戻り値: ==0 正常終了
#     : !=0異常終了
###################################################

echo 'Hulft 起動'

# 必須情報(要メンテナンス) ####################

###
# HulftID $1ならSH起動時にパラメータとして渡す。固定なら直接書き換える
FILE_ID=$1

# HuTftFileName
TO_FILE_NM="EXPORT_ad.csv"

#Hulftディレクトリー
PUT_DIR="/home/batchuser/hulft"

#####:
#ローカル側の作業ディレクトリ
#####
LOCAL_DIR="/var/www/download/data/export/batch/associate"

echo "転送するファイルID: ${FILE_ID}"

FILE_NAME=$(ls -t ${LOCAL_DIR}/*AD* | head -1 | xargs basename)

cp -rf ${LOCAL_DIR}/${FILE_NAME} ${PUT_DIR}/${TO_FILE_NM}

utlidlist i snd -id ${FILE_ID}
utlsend -f ${FILE_ID}

exit $?

