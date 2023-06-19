/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * BatchSettings.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.ksc.batch.util;

import java.io.Serializable;
import java.util.Properties;

import jp.co.ksc.batch.exception.BatRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * バッチの挙動に関する設定を保持するクラス
 *
 * <pre>
 * ★ バッチの挙動に関する設定を保持するクラス
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/09/12 KSC Hiroaki Endo
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class BatchSettings implements Serializable {

    @Autowired
    private Properties props;
    private String osType = "Win32";

    /**
     * コンストラクタ
     *
     * @param p プロパティファイルの内容
     */
    public BatchSettings(Properties p) {
        super();
        this.props = p;
        String osname = System.getProperty("os.name");

        if (osname.indexOf("Windows") >= 0) {
            // Windowsであったときの処理
            this.osType = "Win32";
        } else if (osname.indexOf("Linux") >= 0) {
            // Linuxであったときの処理
            this.osType = "Linux";
        }
    }
}
