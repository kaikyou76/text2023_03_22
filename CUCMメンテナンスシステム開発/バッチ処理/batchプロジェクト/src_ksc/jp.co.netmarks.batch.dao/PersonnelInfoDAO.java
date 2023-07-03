/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * LockFileManager.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.netmarks.batch.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jp.co.netmarks.batch.persistence.LoadPersonnelMapper;
import jp.co.netmarks.util.ErrorMail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 人事情報取込み関連テーブル操作DAOクラス
 *
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/09/12 KSC Hiroaki Endo
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
@Component
public class PersonnelInfoDAO {

    /* ログ出力クラス */
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoadPersonnelMapper lpm;

    @Autowired
    private ErrorMail errMail;

    // *****************
    // CSV処理
    // *****************

    /**
     * Biz系テーブルのCSV取込を行います
     *
     * @param records   データ
     * @param tableName テーブル名
     * @param header    配列
     * @throws SQLException
     */
    public void insertBizTableAll(Map<String, Object>[] records, String tableName, String[] header) throws SQLException {
        Map<String, Object> param = new HashMap<String, Object>();
        if (records != null) {
            try {
                for (int i = 0; i < records.length; i++) {
                    StringBuffer recordLog = new StringBuffer();
                    for (int j = 0; j < header.length; j++) {
                        if (j == 0) {
                            recordLog.append(header[j] + ":" + records[i].get(header[j]));
                        } else {
                            recordLog.append(", " + header[j] + ":" + records[i].get(header[j]));
                        }
                    }
                    param.put(header[j], records[i].get(header[j]));

                    if (tableName.equals("BIZ_AD")) {
                        if (param.get("LAST_NAME") == null)
                            param.put("LAST_NAME", param.get("USER_LOGON_NAME"));
                        if (param.get("FIRST_NAME") == null)
                            param.put("FIRST_NAME", param.get("USER_LOGON_NAME"));
                        lpm.insertBizAD(param);
                    } else if (tableName.equals("BIZ_DEPARTMENT")) {
                        lpm.insertBizDepartment(param);
                    } else if (tableName.equals("BIZ_EMPLOYEE")) {
                        lpm.insertBizEmployee(param);
                    } else if (tableName.equals("BIZ_ORGANIZATION")) {
                        lpm.insertBizOrganization(param);
                    } else if (tableName.equals("BIZ_SHIFT")) {
                        lpm.insertBizShift(param);
                    }
                }
            } catch (Exception e) {
                log.warn("BIZテーブル[" + tableName + "]登録エラー");
                throw e;
            }
        }
    }
	
	//**************************************
	//閾値
	//**************************************
    /**
     * 閾値取得
     * @return 閾値
     * @throws SQLException
     */
    public int getThreshold() throws SQLException {
        int thresholdValue = 0;
        Map<String, Object> record = lpm.selectThreshold();
        if (record != null) {
            Object oVal = record.get("threshold_value");
            if (oVal != null) {
                try {
                    thresholdValue = Integer.valueOf(oVal.toString()).intValue();
                } catch (Exception e) {
                    log.debug("THRESHOLD_VALUE取得で例外:" + e);
                }
            }
        }
        return thresholdValue;
    }
	
    //***********************
    // 閾値用計測(入社数)
    // **********************

    /**
     * 入社数取得
     * @return 入社数
     * @throws SQLException
     */
    public int getEnterCount() throws SQLException {
        int record = 0;

        try {
            record = lpm.selectEnterCount();
        } catch (Exception e) {
            log.debug("閾値用計測(入社数)取得で例外:" + e);
        }
        return record;
    }
    
    public List<Map<String, Object>> getEnterEmployeeList() throws SQLException {
        return lpm.selectEnterEmployee();
    }

	/**
	*BizOrganizationの削除
	*@throws SQLException
	*/
	public void deleteBizOrganization() throws SQLException {
		try {
			Ipm.deleteDumOrganization();
		} catch (Exception e) {
			log.warn("BIZ_ORGANIZATION 削除エラー:DUM_ORGANIZATION");
			throw e;
		}
	}

	/**
	*BizDepartmentの削除
	*@throws SQLException
	*/
	public void deleteBizDepartment() throws SQLException {
		try {
			Ipm.deleteDumDepartment();
		} catch (Exception e) {
			log.warn("BIZ_DEPARTMENT 削除エラー:DUM_DEPARTMENT");
			throw e;
		}
	}
//***************************************
//退社リスト出力
//***************************************
/**
*退社リスト出力
*@退社リスト出力
*@throws SQLException
*/
public Map<String, Object> [] selRetireUser() throws SQLException {
Map<String, Object>[] records;
records = lpm.selAllRetireUserList();
return records;
}
	

}

	

