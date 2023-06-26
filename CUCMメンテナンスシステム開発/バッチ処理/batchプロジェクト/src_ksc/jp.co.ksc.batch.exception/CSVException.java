/**
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * CSVException.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.ksc.batch.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * csvファイルに異常があった場合に報告される例外クラス。
 *
 * <pre>
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/09/12 KSC Hiroaki Endo FX
 * </pre>
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class CSVException extends Exception {

    /** 元の例外 */
    private Throwable th;

    /**
     * デフォルトコンストラクタ
     */
    public CSVException() {
        super();
    }

    /**
     * デフォルトコンストラクタ
     * @param message 詳細メッセージ
     */
    public CSVException(String message) {
        super(message);
    }

    /**
     * デフォルトコンストラクタ
     * @param cause 原因となった例外
     */
    public CSVException(Throwable cause) {
        th = cause;
    }

    /**
     * デフォルトコンストラクタ
     * @param message 詳細メッセージ
     * @param cause 原因となった例外
     */
    public CSVException(String message, Throwable cause) {
        super(message);
        th = cause;
    }

    /**
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        super.printStackTrace();
        System.err.println("*** Root Cause >>> ");
        if (th != null) {
            th.printStackTrace();
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        s.println("*** Root Cause >>> ");
        if (th != null) {
            th.printStackTrace(s);
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        s.println("*** Root Cause >>> ");
        if (th != null) {
            th.printStackTrace(s);
        }
    }
}
