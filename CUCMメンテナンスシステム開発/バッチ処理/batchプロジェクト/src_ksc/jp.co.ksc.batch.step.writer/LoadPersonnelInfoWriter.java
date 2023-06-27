/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * LoadPersonnelInfoWriter.java
 *
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
 * 人事情報取込み用 Jobwriter
 *
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo
 *
 */
public class LoadPersonnelInfoWriter implements ItemWriter<Object> {

    private static final Log log = LogFactory.getLog(LoadPersonnelInfoWriter.class);

    @Autowired
    private Properties properties;

    @Autowired
    private LoadPersonnelInfoLogic lpl;

    @Autowired
    private PersonnelInfoDAO lpdao;

    @Autowired
    private LoadPersonnelMapper lpMapper;

    @Autowired
    private VacuumDAO vd;

    @Autowired
    private ErrorMail errMail;

    /**
     * 人事情報取込処理メイン
     * 
     * @param paramList パラメーター
     * @throws Exception
     */
    @Override
    public void write(List<?> paramList) throws Exception {
        // ロックファイルを確認
        BatchSettings bs = new BatchSettings(properties);
        try {
            LockFileManager.lock(bs);
        } catch (IOException ex) {
            throw new BatRuntimeException(ex.getMessage(), ex);
        }
        try {
            String impcsvPath = bs.getCsvFtpDir();
            String fileEoFAd = bs.getEofAd();
            String fileEoFAm = bs.getEofAm();
            String bizAdFileNM = bs.getBizAdCsvFileName();
            String bizDeptFileNM = bs.getBizDepartmentCsvFileName();
            String bizEmpFileNM = bs.getBizEmployeeCsvFileName();
            String bizOrgFileNM = bs.getBizOrganizationCsvFileName();
            String bizShiftFileNM = bs.getBizShiftCsvFileName();

            String[] fileNames = { 
                impcsvPath + fileEoFAD,
                impcsvPath + fileEoFAM,
                impcsvPath + bizAdFileNM,
                impcsvPath + bizDept FileNM,
                impcsvPath + bizEmpFileNM,
                impcsvPath + bizOrgFileNM 
            };

            if (!lpl.existsIndispensableCsvFile(fileNames)) {
                log.warn("人事情報取込み対象無");
                throw new BatRuntimeException();
            } else {
                // 取得前にテーブルをクリア
                // BIZ_AD取込み
                lpMapper.deleteBizAD();
                lpl.doBizAd(bs);

                // BIZ_DEPARTMENT取込み
                lpMapper.deleteBizDepartment();
                lpl.doBizDepartment(bs);

                // BIZ_EMPLOYEE取込み
                lpMapper.deleteBizEmployee();
                lpl.doBizEmployee(bs);

                // BIZ_ORGANIZATION取込み
                lpMapper.deleteBizOrganization();
                lpl.doBizOrganization(bs);

                // BIZ_SHIFT取込み
                String[] shiftfileNames = { impcsvPath + bizShiftFileNM };
                lpMapper.deleteBizShift();
                if (lpl.existsIndispensableCsvFile(shiftfileNames)) {
                    lpl.doBizShift(bs);
                } else {
                    log.warn("Shiftファイル無");
                }

                // 閾値
                lp1.threshold();

                int mSectioncnt = 0;
                int cntEmployee = 0;

                // 店部課マスタ追加
                mSectioncnt = lpdao.insertMSection();
                log.info("店部課追加件数:" + mSectioncnt);

                // 店部課マスタ更新
                mSectioncnt = lpdao.updateMSection();
                log.info("店部課情報更新件数:" + mSectioncnt);

                // 退社処理
                cntEmployee = lpdao.retireAppUser();
                log.info("退社人数:" + cntEmployee);

                // 入社処理
                cntEmployee = lpdao.additionAppUser();
                log.info("入社人数:" + cntEmployee);

                // 既存社員のプロパティ更新
                cntEmployee = lpdao.updateAppUser();
                log.info("社員情報更新件数:" + cntEmployee);

                // 挻点統廃合
                lpdao.chgOrganization();

                // 社員の所属追加
                cntEmployee = lpdao.additionRUserSection();
                log.info("社員所属追加件数:" + cntEmployee);

                // 社員の所属変更
                cntEmployee = lpdao.updateRUserSetion();
                log.info("社員所属更新件数:" + cntEmployee);

                // 社員の転出元所属削除
                cntEmployee = lpdao.changePersonnel();
                log.info("社員所属削除件数:" + cntEmployee);

                // FileRename
                lpl.csvFileRename(bs);

                // 退社者リスト出力
                lpl.retiredUserFileOut(bs);

                log.info("Vacuum Analyze開始");
                vd.dayVacuum();
                log.info("Vacuum Analyze終了");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // ロック解除
            LockFileManager.unlock(bs);
        }
    }
}