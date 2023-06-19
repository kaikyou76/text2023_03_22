/*
 * BatRuntimeException.java
 *
 * @date 2013/09/12
 * @version 1.0
 * author KSC Hiroaki Endo
 */

package jp.co.ksc.batch.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * バッチ系プログラムで発生したシステムエラーを報告するための例外クラス
 *
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo
 *
 * author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class BatRuntimeException extends RuntimeException {
    /** 元の例外 */
    private Throwable th;

    /**
     * BatRuntimeException のインスタンスを生成します。
     */
    public BatRuntimeException() {
        super();
    }

    /**
     * 詳細メッセージを保持する BatRuntimeException のインスタンスを生成します。
     * @param message 詳細メッセージ
     */
    public BatRuntimeException(final String message) {
        super(message);
    }

    /**
     * 原因となった例外を保持する BatRuntimeException のインスタンスを生成します。
     * @param cause 原因となった例外
     */
    public BatRuntimeException(final Throwable cause) {
        th = cause;
    }

    /**
     * 詳細メッセージと原因となった例外を保持する BatRuntimeException の
     * インスタンスを生成します。
     * @param message 詳細メッセージ
     * @param cause 原因となった例外
     */
    public BatRuntimeException(final String message, final Throwable cause) {
        super(message);
        th = cause;
    }

    /**
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        super.printStackTrace();
        System.err.println("*** Root Cause >>>");
        if (th != null) {
            th.printStackTrace();
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        s.println("*** Root Cause >>>");
        if (th != null) {
            th.printStackTrace(s);
        }
    }
}
