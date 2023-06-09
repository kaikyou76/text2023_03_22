/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * UserEntity.java
 * @date 2013/08/22
 * @version 1.0
 * @author KSC Yuchiro Yoshida
 */
package jp.co.netmarks.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import jp.co.ksc.model.entity.BaseEntity;

/**
 * APP USERテーブル用モデルクラス
 * < MODIFICATION HISTORY>
 * 1.0 2013/08/22 KSC Yuchiro Yoshida #
 */
public class UserEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 267562951797565777L;
    private BigDecimal appUserId;
    private String userRole;
    private String enabledSharedUse;
    private String fulltimeEmployee;
    private String bizEmployeeId;
    private String loginId;
    private String loginPassword;
    private String cucmLoginId;
    private String cucmLoginPassword;
    private String nameKanji;
    private String nameKana;
    private String birthday;
    private String firstName;
    private String lastName;
    private String pin;
    private String telephoneNumber;
    private String enableCtiApplicationUse;
    private String managerUserId;
    private String department;
    private Timestamp lstupdtPwd;
    private String deleted;
    private String cucmUpdateRequestFlag;
    private String errorFlg;

    /**
     * @return appUserId
     */
    public BigDecimal getAppUserId() {
        return appUserId;
    }

    /**
     * @param appUserId the appUserId to set
     */
    public void setAppUserId(BigDecimal appUserId) {
        this.appUserId = appUserId;
    }

    /**
     * @return userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * @return enabledSharedUse
     */
    public String getEnabledSharedUse() {
        return enabledSharedUse;
    }

    /**
     * @param enabledSharedUse the enabledSharedUse to set
     */
    public void setEnabledSharedUse(String enabledSharedUse) {
        this.enabledSharedUse = enabledSharedUse;
    }

    /**
     * @return fulltimeEmployee
     */
    public String getFulltimeEmployee() {
        return fulltimeEmployee;
    }

    /**
     * @param fulltimeEmployee the fulltimeEmployee to set
     */
    public void setFulltimeEmployee(String fulltimeEmployee) {
        this.fulltimeEmployee = fulltimeEmployee;
    }

    /**
     * @return bizEmployeeId
     */
    public String getBizEmployeeId() {
        return bizEmployeeId;
    }

    /**
     * @param bizEmployeeId the bizEmployeeId to set
     */
    public void setBizEmployeeId(String bizEmployeeId) {
        this.bizEmployeeId = bizEmployeeId;
    }

    /**
     * @return loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * @return loginPassword
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * @param loginPassword the loginPassword to set
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * @return cucmLoginId
     */
    public String getCucmLoginId() {
        return cucmLoginId;
    }

    /**
     * @param cucmLoginId the cucmLoginId to set
     */
    public void setCucmLoginId(String cucmLoginId) {
        this.cucmLoginId = cucmLoginId;
    }

    /**
     * @return cucmLoginPassword
     */
    public String getCucmLoginPassword() {
        return cucmLoginPassword;
    }

    /**
     * @param cucmLoginPassword the cucmLoginPassword to set
     */
    public void setCucmLoginPassword(String cucmLoginPassword) {
        this.cucmLoginPassword = cucmLoginPassword;
    }

    /**
     * @return nameKanji
     */
    public String getNameKanji() {
        return nameKanji;
    }

    /**
     * @param nameKanji the nameKanji to set
     */
    public void setNameKanji(String nameKanji) {
        this.nameKanji = nameKanji;
    }

    /**
     * @return nameKana
     */
    public String getNameKana() {
        return nameKana;
    }

    /**
     * @param nameKana the nameKana to set
     */
    public void setNameKana(String nameKana) {
        this.nameKana = nameKana;
    }

    /**
     * @return birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * @return telephoneNumber
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * @param telephoneNumber the telephoneNumber to set
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * @return enableCtiApplicationUse
     */
    public String getEnableCtiApplicationUse() {
        return enableCtiApplicationUse;
    }

    /**
     * @param enableCtiApplicationUse the enableCtiApplicationUse to set
     */
    public void setEnableCtiApplicationUse(String enableCtiApplicationUse) {
        this.enableCtiApplicationUse = enableCtiApplicationUse;
    }

    /**
     * @return managerUserId
     */
    public String getManagerUserId() {
        return managerUserId;
    }

    /**
     * @param managerUserId the managerUserId to set
     */
    public void setManagerUserId(String managerUserId) {
        this.managerUserId = managerUserId;
    }

    /**
     * @return department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return lstupdtPwd
     */
    public Timestamp getLstupdtPwd() {
        return lstupdtPwd;
    }

    /**
     * @param lstupdtPwd the lstupdtPwd to set
     */
    public void setLstupdtPwd(Timestamp lstupdtPwd) {
        this.lstupdtPwd = lstupdtPwd;
    }

    /**
     * @return deleted
     */
    public String getDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    /**
     * @return cucmUpdateRequestFlag
     */
    public String getCucmUpdateRequestFlag() {
        return cucmUpdateRequestFlag;
    }

    /**
     * @param cucmUpdateRequestFlag the cucmUpdateRequestFlag to set
     */
    public void setCucmUpdateRequestFlag(String cucmUpdateRequestFlag) {
        this.cucmUpdateRequestFlag = cucmUpdateRequestFlag;
    }

    /**
     * @return errorFlg
     */
    public String getErrorFlg() {
        return errorFlg;
    }

    /**
     * @param errorFlg the errorFlg to set
     */
    public void setErrorFlg(String errorFlg) {
        this.errorFlg = errorFlg;
    }
}
