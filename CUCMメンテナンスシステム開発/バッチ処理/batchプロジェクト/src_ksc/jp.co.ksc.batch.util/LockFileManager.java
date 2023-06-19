package jp.co.ksc.batch.util;

import java.io.File;
import java.io.IOException;
import jp.co.ksc.batch.exception.AlreadyExecutedException;

/**
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
public final class LockFileManager {

    /**
     * デフォルトコンストラクタ
     */
    private LockFileManager() {
        super();
    }

    /**
     * ロックをかけます。
     *
     * @param setting BatchSettings
     * @throws AlreadyExecutedException すでにロックがかかっていた場合に報告されます。
     * @throws IOException              ファイル入出力エラー
     */
    public static synchronized void lock(BatchSettings setting)
            throws AlreadyExecutedException, IOException {
        File lock = new File(setting.getLockFile());
        if (lock.isFile()) {
            throw new AlreadyExecutedException("すでにバッチが起動しています。");
        } else {
            if (!lock.getParentFile().isDirectory()) {
                lock.getParentFile().mkdirs();
            }
            lock.createNewFile();
        }
    }

    /**
     * ロックを解除します。
     *
     * @param setting BatchSettings
     */
    public static synchronized void unlock(BatchSettings setting) {
        File lock = new File(setting.getLockFile());
        if (lock.isFile()) {
            lock.delete();
        }
    }

    /**
     * ロックされているかを確認します。
     *
     * @param setting BatchSettings
     * @return true ロックされています。false: ロックされていません
     */
    public static boolean isLocked(BatchSettings setting) {
        File lock = new File(setting.getLockFile());
        return lock.isFile();
    }
}
