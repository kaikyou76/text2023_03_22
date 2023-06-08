!/bin/sh
####################################################
#
# 機能概要  : AD情報CSV出力 JOB起動工儿
#
# 引数     : 無
#
# 戻り値    : ==0 正常終了
#         : !=0 異常終了
#
####################################################
#---------------#
#  環境変数読み込み  #
#---------------#
. /home/batchuser/job/profile/.profile
#
#

#-----------#
#  JOBの実行  #
#-----------#
java -cp ${CLASSPATH} org.springframework.batch.core.launch.support.CommandLineJobRunner classpath:/launch-context.xml csvADExport 
LOG_FILE_NAME=`ls -t /var/www/download/logs/batch/*AD* | head -1`
. /home/batchuser/job/CSVHulft AD.sh >> ${LOG_FILE_NAME}
exit $?

########################---解説---##########################

#17行目の . /home/batchuser/job/profile/.profile は、環境変数を読み込むためにプロファイルを実行しています。
#24行目の classpath:/launch-context.xml は、実行するジョブの設定です。
#24行目の csvADExport は、CommandLineJobRunner に渡されるジョブ名です。
#2５行目の `ls -t /var/www/download/logs/batch/*AD* | head -1`は以下の説明します
　　#　batch/ディレクトリ内のファイルのうち、名前に AD という文字列が含まれるものを列挙し,そして先頭の行（つまり、最新のファイル名）を取得します。
#CSVHulft AD.sh スクリプトを実行して、その結果をログファイルに追記します。