/*
*Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
* CSVUtil.java
* @date 2013/09/12
* @version 1.0
* @author KSC Hiroaki Endo
*/
package jp.co.ksc.batch.util;

import java.io.BufferedReader; import java.io.BufferedWriter; import java.io.File;
import java.io.FileInputStream; import java.io.FileOutputStream; import java.io.IOException;
import java.io.InputStreamReader; import java.io.OutputStreamWriter; import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import jp.co.ksc.batch.exception.CSVException;
import org.springframework.util.StringUtils;

/**
* <pre>
csvファイルのユーティリティ
&lt;MODIFICATION HISTORY&gt;
* 1.0 2013/09/12 KSC Hiroaki Endo #
* </pre>
* @author KSC Hiroaki Endo
* @version 1.0 2013/09/12
*/
public class CSVUtil {
    /** 改行コード */
    private static final String RETURN_CD = "\r\n";

    /** 標準の区切り文字 */
    private static final String DEFAULT_DELIM = ",";

    /** Nullを表現する文字列 */
    private static final String DEFAULT_NULL_VALUE = "";

    /** CSVファイル名 */
    private String fileName;

    /** ヘッダー情報 */
    private List<String> headerList = new ArrayList<String>();

    /** データ */
    private Map<String, Object>[] dataMap;

    /** 各値の最大サイズ */
    private List<Object> sizeList = new ArrayList<Object>();

    /** ヘッダーフラグ */
    private boolean hasHeader = false;

    /** 囲み文字フラグ */
    private String quotation = "";

    /** CSVヘッダー出力を設定値にする */
    private boolean headerOutPlain = false;

    /** MYヘッダー情報 */
    private List<String> myHeaderList = new ArrayList<String>();

    /** 区切り文字 */
    private String delim = DEFAULT_DELIM;

    /** Null値を表現する文字列 */
    private String nullValue = DEFAULT_NULL_VALUE;

    /**
     * デフォルトコンストラクタ
     * @param data csvファイルの情報 (ヘッダーと値が対になっているMAP)
     */
    public CSVUtil(final Map<String, Object>[] data) {
        super();
        dataMap = data;
    }
	
    /**
     * デフォルトコンストラクタ
     * @param fileName csvファイル名 
     */
    public CSVUtil(final String fileName) {
        super();
        this.fileName = fileName;
    }	

    /**
     * ヘッダーフラグを設定します。
     * @param hasHeader ヘッダーフラグ
     */
    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    /**
     * ヘッダーフラグを取得します。
     * @return ヘッダーフラグ
     */
    public boolean getHasHeader() {
        return hasHeader;
    }

    /**
     * 区切り文字を取得します。
     * @return 区切り文字
     */
    public String getDelim() {
        return delim;
    }

    /**
     * 区切り文字を設定します。
     * @param delim 区切り文字
     */
    public void setDelim(String delim) {
        this.delim = delim;
    }

    /**
     * 囲み文字フラグを取得します。
     * @return 囲み文字フラグ
     */
    public String getQuotation() {
        if (quotation == null) {
            return "\"";
        }
        return quotation;
    }

    /**
     * 囲み文字フラグを設定します。
     * @param quotation 囲み文字フラグ
     */
    public void setQuotation(String quotation) {
        this.quotation = quotation;
    }

    /**
     * CSVファイル名を設定します。
     * @param fileName CSVファイル名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * CSVファイル名を取得します。
     * @return CSVファイル名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * ヘッダー情報を設定します。
     * @param header ヘッダー情報
     */
    public void addHeader(String header) {
        headerList.add(header);
        sizeList.add(new NullValue());
    }

    /**
     * ヘッダー情報を設定します。
     * @param header ヘッダー情報
     * @param size 値の最大サイズ(バイト)
     */
    public void addHeader(String header, int size) {
        headerList.add(header);
        sizeList.add(size);
    }

    /**
     * ヘッダー情報を取得します。
     * @return ヘッダー情報
     */
    public List<String> getHeaderList() {
        return headerList;
    }

    /**
     * CSVヘッダーを設定値で出力するかを設定します。
     * @param headerOutPlain
     */
    public void setHeaderOutPlain(boolean headerOutPlain) {
        this.headerOutPlain = headerOutPlain;
    }

    /**
     * CSVヘッダーを設定値で出力するかを取得します。
     * @return boolean
     */
    public boolean getHeaderOutPlain() {
        return headerOutPlain;
    }

    /**
     * ヘッダー情報を設定します。
     * @param header ヘッダー情報
     */
    public void myAddHeader(String header) {
        myHeaderList.add(header);
        sizeList.add(new NullValue());
    }

    /**
     * CSV形式の文字列を取得します。
     * @return CSV形式の文字列
     */
    public String getCSVString() {
        StringBuffer buf = new StringBuffer();
        // ヘッダー
        if (getHasHeader()) {
            if (getHeaderOutPlain()) {
                for (int i = 0; i < myHeaderList.size(); i++) {
                    buf.append(getQuotation());
                    buf.append(myHeaderList.get(i));
                    buf.append(getQuotation());
                    if (i == myHeaderList.size() - 1) {
                        buf.append(getDelim());
                    }
                }
            } else {
                for (int i = 0; i < headerList.size(); i++) {
                    buf.append(getQuotation());
                    buf.append(headerList.get(i));
                    buf.append(getQuotation());
                    if (i != headerList.size() - 1) {
                        buf.append(getDelim());
                    }
                }
            }
            buf.append(RETURN_CD);
        }
        // 値
        for (int i = 0; i < dataMap.length; i++) {
            for (int j = 0; j < headerList.size(); j++) {
                buf.append(getQuotation());
                buf.append(replaceStr(dataMap[i].get(headerList.get(j).toString()), sizeList.get(j)));
                buf.append(getQuotation());
                if (headerList.size() - 1 != j) {
                    buf.append(getDelim());
                }
            }
            buf.append(RETURN_CD);
        }
        return buf.toString();
    }

    /**
     * CSVファイルをHDDに書き込みます。
     * @throws IOException 書き込みエラー
     */
    public void write() throws IOException {
        String csv = getCSVString();
        File file = new File(fileName);
        file.delete();
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "MS932");
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(csv, 0, csv.length());
        bw.flush();
        bw.close();
    }

    /**
     * CSVファイルをHDDから読み込みます。
     * @throws IOException 読み込みエラー
     * @throws CSVException CSVファイルに異常がある場合
     */
    @SuppressWarnings({ "resource", "unchecked" })
    public void read() throws IOException, CSVException {
        String s;
        String tmp;
        boolean read = false;
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "MS932");
        BufferedReader br = new BufferedReader(isr);

        List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
        Map<String, Object> value = null;

        int lineCnt = 0;
        int cnt = 0;
        while ((s = br.readLine()) != null) {
            lineCnt++;

            cnt = 0;
            s = "<" + StringUtils.replace(s, getDelim(), ">" + getDelim() + "<") + ">";
            StringTokenizer st = new StringTokenizer(s, getDelim());

            // ヘッダー行
            if (getHasHeader() && !read) {
                headerList = new ArrayList<String>();
                while (st.hasMoreTokens()) {
                    tmp = st.nextToken();
                    tmp = tmp.substring(1, tmp.length() - 1);

                    // 囲み文字の消去
                    if (!"".equals(getQuotation()) && tmp.startsWith(getQuotation()) && tmp.endsWith(getQuotation())) {
                        tmp = tmp.substring(1, tmp.length() - 1);
                    }
                    headerList.add(tmp);
                }
                read = true;
            }
            // 値行
            else {
                if (st.countTokens() != headerList.size()) {
                    throw new CSVException(
                            "ヘッダーの数と値の数が一致していません。 [" + lineCnt + "]行目 headerList.size:[" + headerList.size() + "],countTokens:[" + st.countTokens() + "]");
                }
                value = new HashMap<String, Object>();

                while (st.hasMoreTokens()) {
                    tmp = st.nextToken();
                    tmp = tmp.substring(1, tmp.length() - 1);

                    // 囲み文字の消去
                    if (!"".equals(getQuotation()) && tmp.startsWith(getQuotation()) && tmp.endsWith(getQuotation())) {
                        tmp = tmp.substring(1, tmp.length() - 1);
                    }
                    if (tmp.equals(getNullValue())) {
                        tmp = null;
                    }

                    value.put(headerList.get(cnt), tmp);
                    cnt++;
                }
                valueList.add(value);
            }
        }
        br.close();
        isr.close();
        fis.close();
        dataMap = (Map<String, Object>[]) valueList.toArray(new Map[0]);
    }

    /**
     * フォーマット変換を行います。
     * @param argSource 変換対象のオブジェクト
     * @param size 値の最大サイズ
     * @return フォーマットされた文字列
     */
    public String replaceStr(final Object argSource, final Object size) {
        String source = null;
        if (argSource == null) {
            return getNullValue();
        } else if (argSource instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sDate = sdf.format((Date) argSource).toString();
            source = sDate;
        } else {
            source = argSource.toString();
            source = StringUtils.replace(source, "\n", "");
            source = StringUtils.replace(source, "\r", "");
            source = StringUtils.replace(source, ",", ".");
            source = StringUtils.replace(source, "\"", "");

            // 最大サイズ以上は切り捨て
            if (size instanceof Integer) {
                source = source.substring(0, ((Integer) size).intValue());
            }
        }
        return source;
    }


    /**
     * CSV ファイルの内容を Map 配列で取得します。
     * @return CSV ファイルの内容
     */
    public Map<String, Object>[] getDataMaps() {
        return dataMap;
    }

    /**
     * CSV ファイルの内容を Map 配列で設定します。
     * @param dataMap CSV ファイルの内容
     */
    public void setDataMaps(Map<String, Object>[] dataMap) {
        this.dataMap = dataMap;
    }

    /**
     * CSV ファイルが存在するかを確認します。
     * @return true: 存在する, false: 存在しない
     */
    public boolean existFile() {
        File file = new File(fileName);
        return file.isFile();
    }

    /**
     * Null 値を表現する文字列を取得します。
     * @return Null 値を表現する文字列
     */
    public String getNullValue() {
        return nullValue;
    }

    /**
     * Null 値を表現する文字列を設定します。
     * @param nullValue Null 値を表現する文字列
     */
    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    /**
     * timestamp を付与した FileNM を取得します。
     * @param fileName 元のファイル名
     * @return FileName_YYYYMMDDHHMMSS.XXX
     */
    public String getTimestampAddFileNM(String fileName) {
        if (fileName == null) return "";

        String[] fileSplit = fileName.split("\\.", 0);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String ymdhms = sdf.format(cal.getTime());

        return fileSplit[0] + "_" + ymdhms + "." + fileSplit[1];
    }

    /**
     * Export 用 FileNM を取得します。
     * @param fileName 元のファイル名
     * @return FileName_EXPORTED_YYYYMMDDHHMMSS.xxx
     */
    public String getExpTimeAddFileNM(String fileName) {
        if (fileName == null) return "";

        String[] fileSplit = fileName.split("\\.", 0);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String ymdhms = sdf.format(cal.getTime());

        return fileSplit[0] + "_EXPORTED_" + ymdhms + "." + fileSplit[1];
    }

    /**
     * Import 用 FileNM を取得します。
     * @param fileName 元のファイル名
     * @return FileName_IMPORTED_YYYYMMDDHHMMSS.xxx
     */
    public String getImpTimeAddFileNM(String fileName) {
        if (fileName == null) return "";

        String[] fileSplit = fileName.split("\\.", 0);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String ymdhms = sdf.format(cal.getTime());

        return fileSplit[0] + "_IMPORTED_" + ymdhms + "." + fileSplit[1];
    }
	
}

/**
 * Nullを表現するクラス
 */
final class NullValue {
}
