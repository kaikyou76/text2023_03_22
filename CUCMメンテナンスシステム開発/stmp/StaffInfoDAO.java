package jp.co.batch.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.batch.persistence.LoadStaffMapper;

/**
 * <pre>
 * 人事情報取込み関連テーブル操作DAOクラス
 * </pre>
 * <p>Title: StaffInfoDAO.java</p>
 * Jun 25, 2023
 *@author Yao KaiKyou
 */
public class StaffInfoDAO {

	/* ログ出力クラス */
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private LoadStaffMapper lsm;
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
	public void insertBizTableAll(Map<String, Object>[] records, String tableName, String[] header)
			throws SQLException {
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

						param.put(header[j], records[i].get(header[j]));
					}

					if (tableName.equals("BIZ_AD")) {
						if (param.get("LAST_NAME") == null)
							param.put("LAST_NAME", param.get("USER_LOGON_NAME"));
						if (param.get("FIRST_NAME") == null)
							param.put("FIRST_NAME", param.get("USER_LOGON_NAME"));
						lsm.insertBizAD(param);
					} else if (tableName.equals("BIZ_DEPARTMENT")) {
						lsm.insertBizDepartment(param);
					} else if (tableName.equals("BIZ_EMPLOYEE")) {
						lsm.insertBizEmployee(param);
					} else if (tableName.equals("BIZ_ORGANIZATION")) {
						lsm.insertBizOrganization(param);
					} else if (tableName.equals("BIZ_SHIFT")) {
						lsm.insertBizShift(param);
					}
				}
			} catch (Exception e) {
				log.warn("BIZテーブル[" + tableName + "]登録エラー");
				throw e;
			}
		}
	}

	/**
	*BizOrganizationの削除
	*@throws SQLException
	*/
	public void deleteBizOrganization() throws SQLException {
		try {
			lsm.deleteDumOrganization();
		} catch (Exception e) {
			log.warn("BIZ_ORGANIZATION 削除エラー:DUM_ORGANIZATION");
			throw e;
		}
	}

	/**
	*BizDepartmentの削除
	*@throws SQLException
	*/
//	public void deleteBizDepartment() throws SQLException {
//		try {
//			Ipm.deleteDumDepartment();
//		} catch (Exception e) {
//			log.warn("BIZ_DEPARTMENT 削除エラー:DUM_DEPARTMENT");
//			throw e;
//		}
//	}
}
