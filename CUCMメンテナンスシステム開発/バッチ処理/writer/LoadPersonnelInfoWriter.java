/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * LoadPersonnelInfoWriter.java
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */
package jp.co.ksc.batch.step.writer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import jp.co.ksc.batch.exception.BatRuntimeException;
import jp.co.ksc.batch.util.BatchSettings;
import jp.co.ksc.batch.util.LockFileManager;
import jp.co.netmarks.batch.component.LoadPersonnelInfoLogic;
import jp.co.netmarks.batch.dao.PersonnelInfoDAO;
import jp.co.netmarks.batch.dao.VacuumDAO;
import jp.co.netmarks.batch.persistence.LoadPersonnelMapper;
import jp.co.netmarks.util.ErrorMail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 人事情報取込用のJobWriter
 *
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo
 *
 * @version 1.0 2013/09/12
 */
public class LoadPersonnelInfoWriter implements ItemWriter<Object> {
    private static final Log log = LogFactory.getLog(LoadPersonnelInfoWriter.class);

    @Autowired
    private Properties properties;

    @Autowired
    private LoadPersonnelInfoLogic logic;

    @Autowired
    private PersonnelInfoDAO dao;

    @Autowired
    private LoadPersonnelMapper mapper;

    @Autowired
    private VacuumDAO vacuumDao;

    @Autowired
    private ErrorMail errorMail;

    /**
     * 人事情報取込処理メイン
     *
     * @param paramList パラメータリスト
     * @throws Exception 例外
     */
    @Override
    public void write(List<?> paramList) throws Exception {
        BatchSettings batchSettings = new BatchSettings(properties);

        // ロックファイルを確認
        try {
            LockFileManager.lock(batchSettings);
        } catch (IOException ex) {
            log.error("ロックファイルの確認中にエラーが発生しました", ex);
            throw new BatRuntimeException(ex.getMessage(), ex);
        }

        try {
            String importPath = batchSettings.getCsvFtpDir();
            String fileEoFAd = batchSettings.getEofAd();
            String fileEoFAm = batchSettings.getEofAm();
            String bizAdFileNM = batchSettings.getBizAdCsvFileName();
            String bizDeptFileNM = batchSettings.getBizDepartmentCsvFileName();
            String bizEmpFileNM = batchSettings.getBizEmployeeOsvFileName();
            String bizOrgFileNM = batchSettings.getBizOrganizationCsvFileName();
            String bizShiftFileNM = batchSettings.getBizShiftCsvFileName();

            String[] fileNames = {
                importPath + fileEoFAd,
                importPath + fileEoFAm,
                importPath + bizAdFileNM,
                importPath + bizDeptFileNM,
                importPath + bizEmpFileNM,
                importPath + bizOrgFileNM
            };

            if (!logic.existsIndispensableCsvFile(fileNames)) {
                log.warn("人事情報取込み対象が存在しません");
                throw new BatRuntimeException();
            } else {
                // 取得前にテーブルをクリア
                // BIZ_AD取込
                mapper.deleteBizAD();
                logic.doBizAd(batchSettings);

                // BIZ_DEPARTMENT取込
                mapper.deleteBizDepartment();
                logic.doBizDepartment(batchSettings);

                // BIZ_EMPLOYEE取込
                mapper.deleteBizEmployee();
                logic.doBizEmployee(batchSettings);

                // BIZ_ORGANIZATION取込
                mapper.deleteBizOrganization();
                logic.doBizOrganization(batchSettings);

                // BIZ_SHIFT取込
                String[] shiftFileNames = { importPath + bizShiftFileNM };
                mapper.deleteBizShift();
                if (logic.existsIndispensableCsvFile(shiftFileNames)) {
                    logic.doBizShift(batchSettings);
                } else {
                    log.warn("Shiftファイルが存在しません");
                }

                // 閾値
                logic.threshold();

                int mSectionCnt = 0;
                int cntEmployee = 0;

                // 店部課マスタ追加
                mSectionCnt = dao.insertMSection();
                log.info("店部課追加件数: " + mSectionCnt);

                // 店部課マスタ更新
                mSectionCnt = dao.updateMSection();
                log.info("店部課情報更新件数: " + mSectionCnt);

                // 退社処理
                cntEmployee = dao.retireAppUser();
                log.info("退社人数: " + cntEmployee);

                // 入社処理
                cntEmployee = dao.additionAppUser();
                log.info("入社人数: " + cntEmployee);

                // 既存社員のプロパティ更新
                cntEmployee = dao.updateAppUser();
                log.info("社員情報更新件数: " + cntEmployee);

                // 挻点統合
                dao.chgOrganization();

                // 社員の所属追加
                cntEmployee = dao.additionRUserSection();
                log.info("社員所属追加件数: " + cntEmployee);

                // 社員の所属変更
                cntEmployee = dao.updateRUserSetion();
                log.info("社員所属更新件数: " + cntEmployee);

                // 社員の転出元所属削除
                cntEmployee = dao.changePersonnel();
                log.info("社員所属削除件数: " + cntEmployee);

                // FileRename
                logic.csvFileRename(batchSettings);

                // 退社者リスト出力
                logic.retiredUserFileOut(batchSettings);
            }

            log.info("Vacuum Analyze開始");
            vacuumDao.dayVacuum();
            log.info("Vacuum Analyze終了");
        } catch (Exception e) {
            log.error("人事情報取込処理中にエラーが発生しました", e);
            throw e;
        } finally {
            // ロック解除
            LockFileManager.unlock(batchSettings);
        }
    }
}
