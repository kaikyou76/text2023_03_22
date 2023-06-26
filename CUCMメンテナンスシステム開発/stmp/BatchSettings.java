package jp.co.batch.util;

import java.io.Serializable;
import java.util.Properties;

import jp.co.batch.exception.BatRuntimeException;

public class BatchSettings implements Serializable {

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

    /**
     * 引数なしコンストラクタ
     */
    public BatchSettings() {
        this(loadProperties());
    }

    private static Properties loadProperties() {
        // プロパティファイルの読み込み処理を実装する
        Properties properties = new Properties();
        // プロパティファイルをロードするコードを追加する
        return properties;
    }

    /**
     * super();
     * ★ プロパティファイルの特定 KEY情報を取得
     *
     * @param key
     * @return 特定KEYのプロパティ情報
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * ★ ロックファイルのパスを取得します。
     *
     * @return ロックファイルのパス
     */
    public String getLockFile() {
        String value = props.getProperty("LockFile." + osType);
        if (value == null || value.equals("")) {
        	throw new BatRuntimeException("プロパティファイルが正しくロードされていません。");
        }
        return value;
    }

    /**
     * ★ CSVInputディレクトリの取得
     *
     * @return FTP
     */
    public String getInputDir() {
        String value = props.getProperty("InputDir." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.properties InputDir の定義がありません。");
        }
        return value;
    }
    /**
     * CSVFTP先ディレクトリの取得
     *
     * @return FTP ディレクトリ
     */
    public String getCsvFtpDir() {
        return getInputDir();
    }

    /**
     * SVImport Complete ディレクトリの取得
     *
     * @return FTP ディレクトリ
     */
    public String getInputCompDir() {
        String value = props.getProperty("InputCompDir." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにInputDirの定義がありません。");
        }
        return value;
    }

    /**
     * ★ ReceiveDirディレクトリの取得
     *
     * @return ReceiveDirディレクトリ
     */
    public String getReceiveDir() {
        String value = props.getProperty("ReceiveDir." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにReceiveDirtの定義がありません。");
        }
        return value;
    }

    /**
     * Outputディレクトリの取得
     *
     * @return Output
     */
    public String getOutputDir() {
        String value = props.getProperty("OutputDir." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにOutputDirの定義がありません。");
        }
        return value;
    }

    /**
     * DBCSV Outputディレクトリの取得
     *
     * @return Outputディレクトリ
     */
    public String getOutputDirDB() {
        String value = props.getProperty("OutputDir." + osType);
        value += props.getProperty("OutputDir2." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにOutputDir2の定義がありません。");
        }
        return value;
    }

    /**
     * Associatecsv outputディレクトリの取得
     *
     * @return Outputディレクトリ
     */
    public String getOutputDirAssociate() {
        String value = props.getProperty("OutputDir." + osType);
        value += props.getProperty("OutputDir3." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.properties OutputDir3の定義がありません。");
        }
        return value;
    }

    /**
     * Circuitlist CSV Output
     *
     * @return Output1
     */
    public String getOutputDirCircuitlist() {
        String value = props.getProperty("OutputDir." + osType);
        value += props.getProperty("OutputDir4." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.properties OutputDir4の定義がありません。");
        }
        return value;
    }

    /**
     * RetireCsv outputディレクトリの取得
     *
     * @return Outputディレクトリ
     */
    public String getOutputRetireDir() {
        String value = props.getProperty("OutputRetireDir." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにOutputRetireDirの定義がありません。");
        }
        return value;
    }

    /*
     * ユーザーと電話機の一覧 エクスポート/インポートディレクトリの取得
     *
     * @return FTP ディレクトリ
     */
    public String getManageSearchCsvDir() {
        String value = props.getProperty("ManageSearchCsvDir." + osType);
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにManageSearchCsvDirの定義がありません。");
        }
        return value;
    }

    /**
     * ファイルディレクトリのセパレータを取得します。
     *
     * @return ファイルディレクトリのセパレータ
     */
    public String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * BizOrganizationのcsvファイル名を取得します。
     *
     * @return CSV71
     */
    public String getBizOrganizationCsvFileName() {
        String value = props.getProperty("BizOrganizationCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにBizOrganizationCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * BizDepartmentのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getBizDepartmentCsvFileName() {
        String value = props.getProperty("BizDepartmentCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにBizDepartmentCsvFileNameの定義がありません。");
        }
        return value;
    }

    /*
     * BizEmployeeのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getBizEmployeeCsvFileName() {
        String value = props.getProperty("BizEmployeeCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにBizEmployeeCsvFileNameの定義がありません。");
        }
        return value;
    }

    /*
     * BizAdのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getBizAdCsvFileName() {
        String value = props.getProperty("BizAdCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.properties BizAdCsvFileName Oh.");
        }
        return value;
    }

    /**
     * TmpIntEmployeeのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getTmpIntEmployeeCsvFileName() {
        String value = props.getProperty("TmpIntEmployeeCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにTmpIntEmployeeCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * TmpIntDepartmentのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getTmpIntDepartmentCsvFileName() {
        String value = props.getProperty("TmpIntDepartmentCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにTmpIntDepartmentCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * TmpAdのcsvファイル名を取得します。
     *
     * @return CSV
     */
    public String getTmpAdCsvFileName() {
        String value = props.getProperty("TmpAdCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにTmpAdCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * BizShiftのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getBizShiftCsvFileName() {
        String value = props.getProperty("BizShiftCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにBizShiftCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * DumOrganizationのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getDumOrganizationCsvFileName() {
        String value = props.getProperty("DumOrganizationCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにDumOrganizationCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * TmpOrganizationのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getTmpIntOrganizationCsvFileName() {
        String value = props.getProperty("TmpIntOrganizationCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにTmpOrganizationCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * DumDepartmentのcsvファイル名を取得します。
     *
     * @return CSVファイル名
     */
    public String getDumDepartmentCsvFileName() {
        String value = props.getProperty("DumDepartmentCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにDumDepartmentCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * DumEmployeeのcsvファイル名を取得します。
     *
     * @return csvファイル名
     */
    public String getDumEmployeeCsvFileName() {
        String value = props.getProperty("DumEmployeeCsvFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにDumEmployeeCsvFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * EOFADファイル名を取得します。
     *
     * @return ファイル名
     */
    public String getEofAd() {
        String value = props.getProperty("Eof.Ad");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにEof.Adの定義がありません。");
        }
        return value;
    }

    /**
     * EOFAMファイル名を取得します。
     *
     * @return ファイル名
     */
    public String getEofAm() {
        String value = props.getProperty("Eof.Am");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにEof.Amの定義がありません。");
        }
        return value;
    }

    /**
     * RetiredUserの出力ファイル名を取得します。
     *
     * @return RetiredUserの出力ファイル名
     */
    public String getRetiredUserFileName() {
        String value = props.getProperty("RetiredUserFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにRetiredUserFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * JoinedUserの出力ファイル名を取得します。
     *
     * @return JoinedUserの出力ファイル名
     */
    public String getJoinedUserFileName() {
        String value = props.getProperty("JoinedUserFileName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにJoinedUserFileNameの定義がありません。");
        }
        return value;
    }

    /**
     * BizOrganization@Csv Headerを取得
     *
     * @return BizOrganizationCsvHeader
     */
    public String getTmpBizOrganizationCsvHeader() {
        String value = props.getProperty("TmpBizOrganizationCsvHeader");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにBizOrganizationCsvHeadersの定義がありません。");
        }
        return value;
    }

    /**
     * BizOrganizationのテーブル名を取得
     * @return BizOrganizationのテーブル名
     */
    public String getTmpBizOrganizationTableName() {
        String value = props.getProperty("TmpBizOrganizationTableName");
        if (value == null || value.equals("")) {
            throw new BatRuntimeException("environment.propertiesにBizOrganizationTableNameの定義がありません。");
        }
        return value;
    }

}


