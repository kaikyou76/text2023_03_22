/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * VacuumDAO.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.netmarks.batch.dao;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jp.co.netmarks.batch.persistence.LoadPersonnelMapper;
import jp.co.netmarks.batch.persistence.MasterMapper;

/**
 * Vacuum操作DAOクラス
 *
 * <pre>
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/09/12 KSC Hiroaki Endo
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
@Component
public class VacuumDAO {

    /** ログ出力クラス */
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoadPersonnelMapper loadMapper;

    @Autowired
    private MasterMapper masMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    private DefaultTransactionDefinition dtd = null;

    /**
     * 初期化
     */
    private void vacuumInit() {
        dtd = new DefaultTransactionDefinition();
        dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        dtd.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
    }

    /**
     * FullVacuumを実行
     *
     * @throws SQLException
     */
    public void fullVacuum() throws SQLException {
        try {
            vacuumInit();
            TransactionStatus ts = txManager.getTransaction(dtd);
            masMapper.fullVacuum();
            txManager.commit(ts);
        } catch (Exception e) {
            log.warn("Vacuum エラー: FULL Vacuum");
            throw e;
        }
    }

    /**
     * 全テーブルのVacuumとAnalyzeを実行
     *
     * @throws SQLException
     */
    public void dayVacuum() throws SQLException {
        try {
            vacuumInit();
            TransactionStatus ts = txManager.getTransaction(dtd);
            masMapper.dayVacuum();
            txManager.commit(ts);
        } catch (Exception e) {
            log.warn("Vacuum エラー: FULL Vacuum");
            throw e;
        }
    }

    /**
     * 人事情報取込系テーブルのVacuum実行
     *
     * @throws SQLException
     */
    public void personnelVacuum() throws SQLException {
        try {
            vacuumInit();
            TransactionStatus ts = txManager.getTransaction(dtd);
            loadMapper.vacuum();
            txManager.commit(ts);
        } catch (Exception e) {
            log.warn("Vacuum エラー: LoadPersonnel");
            throw e;
        }
    }

    /**
     * マスターパラメータ取得系テーブルのVacuum実行
     *
     * @throws SQLException
     */
    public void masterVacuum() throws SQLException {
        try {
            vacuumInit();
            TransactionStatus ts = txManager.getTransaction(dtd);
            masMapper.vacuum();
            txManager.commit(ts);
        } catch (Exception e) {
            log.warn("Vacuum エラー: MasterParameter");
            throw e;
        }
    }
}
