#!/bin/bash

# 監視システムファイル転送シェルの実行
execute_file_transfer_shell() {
  local class_name=$1
  local status=$2
  # 実行コマンドを記述する（例: file_transfer_shell.sh）
  # パラメータとして $class_name と $status を渡す
}

# PostgreSQLの停止
stop_postgresql() {
  # PostgreSQLの実行状態を取得するコマンド（例: pg_ctl status）
  local postgres_status=$(pg_ctl status)
  #[[ ]]は、bashの条件式を記述するための構文です。
  if [[ $postgres_status == *"server is running"* ]]; then
    # PostgreSQLを停止するコマンド（例: pg_ctl stop）
    pg_ctl stop
  fi

  # 終了処理を実行する関数を呼び出す（例: execute_cleanup_process）
  execute_cleanup_process
}

# エラー処理
handle_error() {
  local error_batch=$1

  execute_file_transfer_shell "$error_batch" "NG"

  # 戻り値として9を返却する
  exit 9
}

# 終了処理
handle_exit() {
  local processed_batch=$1

  execute_file_transfer_shell "$processed_batch" "OK"

  # 戻り値として0を返却する
  exit 0
}

# 初期処理
execute_file_transfer_shell "$1" "START"

# PostgreSQLの停止
stop_postgresql

# エラー処理
handle_error "$1"

# 終了処理
handle_exit "$1"

