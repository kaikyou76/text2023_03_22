/*
 * LockFileManager.java
 *
 * <pre>
 * バッチの多重起動を防ぐロックファイルの操作を行うクラス
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
package jp.co.ksc.batch.exception;

/**
 * <pre>
 * バッチ処理を2重起動した場合に報告される例外です。
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo
 * </pre>
 * 
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class AlreadyExecutedException extends Exception {

    /**
     * AlreadyExecutedException のインスタンスを生成します。
     */
    public AlreadyExecutedException() {
        super();
    }

    /**
     * 詳細メッセージを保持する AlreadyExecutedException のインスタンスを生成します。
     * 
     * @param message 詳細メッセージ
     */
    public AlreadyExecutedException(String message) {
        super(message);
    }

    /**
     * 原因となった例外を保持する AlreadyExecutedException のインスタンスを生成します。
     * 
     * @param cause 原因となった例外
     */
    public AlreadyExecutedException(Throwable cause) {
        super(cause);
    }

    /**
     * 詳細メッセージと原因となった例外を保持する AlreadyExecutedException のインスタンスを生成します。
     * 
     * @param message 詳細メッセージ
     * @param cause   原因となった例外
     */
    public AlreadyExecutedException(String message, Throwable cause) {
        super(message, cause);
    }
}
