package jp.co.batch.step.writer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.batch.component.LoadStaffInfoLogic;
import jp.co.batch.dao.StaffInfoDAO;
import jp.co.batch.exception.BatRuntimeException;
import jp.co.batch.persistence.LoadStaffMapper;
import jp.co.batch.util.BatchSettings;
import jp.co.batch.util.LockFileManager;

public class LoadStaffInfoWriter implements ItemWriter<Object> {
	private static final Log log = LogFactory.getLog(LoadPersonnelInfoWriter.class);

	@Autowired
	private Properties properties;

    @Autowired
    private LoadStaffMapper lsMapper;

    @Autowired
    private StaffInfoDAO lsdao;

	private LoadStaffInfoLogic lsl;
	public void write(List<?> plist) throws Exception {
		BatchSettings bs = new BatchSettings(properties);
		// ロックファイルを確認
		try {
			LockFileManager.lock(bs);
			log.info(bs.getProperty("BT 000 1001"));
		} catch (IOException ex) {
			String errorMessage = bs.getProperty("BT 000 E001");
			throw new BatRuntimeException(errorMessage, ex);
		}

	try {
		String impcsvPath = bs.getCsvFtpDir(); //inputDir 変数A
		//String InpCompPath = bs.getInputCompDir(); //退避ファイル格納先 変数B
		//String OupRetirePath = bs.getOutputRetireDir(); // 退職・加入者データ格納先 変数C
		//String fileEoFAd = bs.getEofAd(); //EofAd 変数D
		//String fileEoFAm = bs.getEofAm(); //EofAm 変数E
		//String bizAdFileNM = bs.getTmpAdCsvFileName(); //BizAdのCSVファイル名 変数F
		//String bizDeptFileNM = bs.getTmpIntDepartmentCsvFileName(); //BizDepartmentのCSVファイル名 変数G
		//String bizEmpFileNM = bs.getTmpIntEmployeeCsvFileName(); //BizEmployeeのCSVファイル名 変数H
		String bizOrgFileNM = bs.getTmpIntOrganizationCsvFileName(); //BizOrganizationのCSVファイル名 変数I
		//String rUserFileNM = bs.getRetiredUserFileName(); //退職者情報データファイル名 変数J
		//String jUserFileNM = bs.getJoinedUserFileName(); //加入者情報データファイル名 変数K
		//String receivePath = bs.getReceiveDir(); //ReceiveDir 変数L

		String[] fileName = {impcsvPath + bizOrgFileNM};
		if(!lsl.existsIndispensableCsvFile(fileName)) {
			log.warn("人事情報取込み対象無し");
			throw new BatRuntimeException();
		}else {
            // BIZ_ORGANIZATION取込み
            lsMapper.deleteBizOrganization();
            //lsl.doBizOrganization(bs);
		}
		} catch (Exception e) {
		throw e;} finally {
		// ロック解除
		LockFileManager.unlock (bs);
		}
	}
}
