/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * BaseController.java
 * @date 2013/08/01
 * @version 1.0
 * @author KSC Tomomichi Iwasawa
 */
package jp.co.ksc.controller;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.co.ksc.model.form.GridListForm;
import jp.co.ksc.model.form.list.BaseGridRowForm;
import jp.co.ksc.spring.AuthenticationToken;
import jp.co.ksc.spring.security.TokenManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * <pre>
 * ★コントローラ基底クラス
 *
 * &lt;MODIFICATION HISTORY&gt;
 * 1.0 2013/08/01 KSC Tomomichi Iwasawa
 * </pre>
 * 
 * @author KSC Tomomichi Iwasawa
 * @version 1.0 2013/08/01
 */
public class BaseController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    private Validator validator;

    /**
     * ★エラーメッセージを追加する
     * 
     * @param result BindingResult
     * @param filed  FieldError
     */
    protected void addError(BindingResult result, FieldError filed) {
        result.addError(filed);
    }

    /**
     * ★ エラーメッセージを追加する
     * 
     * @param result  BindingResult
     * @param message エラーメッセージ
     */
    protected void addError(BindingResult result, String message) {
        this.addError(result, new FieldError(StringUtils.EMPTY, "message", message));
    }

    /**
     * this.addError(result, new FieldError(objectName, fieldName, MessageFormat.format(message, arguments)));
     * 
     * ★エラーメッセージを追加する
     * 
     * @param result    BindingResult
     * @param message   エラーメッセージ
     * @param arguments 動的メッセージ
     */
    protected void addError(BindingResult result, String message, Object... arguments) {
        this.addError(result, MessageFormat.format(message, arguments));
    }

    /**
     * ★ エラーメッセージを追加する
     * 
     * @param result     BindingResult
     * @param objectName オブイェクト名
     * @param fieldName  フィールド名
     * @param message    エラーメッセージ
     * @param arguments  動的メッセージ
     */
    protected void addError(BindingResult result, String objectName, String fieldName, String message, Object... arguments) {
        this.addError(result, new FieldError(objectName, fieldName, MessageFormat.format(message, arguments)));
    }

    /**
     * ★ JSON形式のModelAndViewを返却する
     * 
     * @param map マップ
     * @return ModelAndView
     */
    protected ModelAndView getJsonView(Map<String, Object> map) {
        MappingJacksonJsonView mjsv = new MappingJacksonJsonView();
        mjsv.setAttributesMap(map);
        return new ModelAndView(mjsv);
    }

    /**
     * ★ 認証状態にする
     * 
     * @param username ユーザー名
     * @param model    モデル
     */
    protected void setAuthentication(String username, UserDetails model) {
        SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(username, model));
    }

    /**
     * return TokenManager.isValidToken(request);
     * 
     * ★ ワンタイムトークンが一致したか否かを返す
     * 
     * @return true: False:-
     */
    protected boolean isValidToken() {
        return TokenManager.isValidToken(request);
    }

    /**
     * GridList Formのバリデーションを行う
     * 
     * @param form   GridList Form
     * @param result BindingResult
     */
    protected void validateGridListForm(GridListForm form, BindingResult result) {
        /* checkが無い行はValidateエラーにならないようにする */
        for (Object obj : form.getGridForm()) {
            BaseGridRowForm rowForm = (BaseGridRowForm) obj;
            if (!rowForm.isChecked())
                rowForm.convetToNoValidateBean();
        }

        DataBinder binder = new DataBinder(form);
        binder.setValidator(validator);

        /* validate */
        binder.validate();

        BindingResult errorResult = binder.getBindingResult();
        for (ObjectError error : errorResult.getAllErrors()) {
            result.addError(error);
        }
    }
}
