/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 * SharedTelController.java
 * @date 2013/09/14
 * @version 1.0
 * @author KSC Yuichiro Yoshida
 */
package jp.co.netmarks.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import jp.co.ksc.controller.BaseController;
import jp.co.ksc.spring.security.TokenHandler;
import jp.co.ksc.spring.security.TokenValidateType;
import jp.co.netmarks.model.UserAndTelUpdateModel;
import jp.co.netmarks.model.form.SharedTelUpdateForm;
import jp.co.netmarks.service.UserAndTelSearchService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * ★ 共用ユーザー用コントローラー
 * &lt; MODIFICATION HISTORY &gt;
 * 1.0 2013/09/14 KSC Yuichiro Yoshida #1FX
 * </pre>
 * 
 * @author KSC Yuichiro Yoshida
 * @version 1.0 2013/09/14
 */
@Controller
@RequestMapping("/sharedTel")
public class SharedTelController extends BaseController {
    @Autowired
    private UserAndTelSearchService userAndTelSearchService;

    /**
     * 初期表示
     * 
     * @return 遷移先
     * @throws Exception　例外処理
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    protected String index() throws Exception {
        return "dialog/sharedTelEdit";
    }

    /**
     * 共用化処理
     * 
     * @param form   SharedTelUpdateForm
     * @param result BindingResult
     * @return ModelAndView
     * @throws Exception　例外処理
     */
    @TokenHandler(validate = TokenValidateType.REMOVE)
    @RequestMapping(value = "/sharedTelEdit", method = RequestMethod.POST)
    public ModelAndView sharedTelEdit(@Valid SharedTelUpdateForm form, BindingResult result) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        /* 入力チェック */
        if (result.hasErrors()) {
            map.put("errors", result.getAllErrors());
            return getJsonView(map);
        }

        /* Form-Modelコピー処理 */
        UserAndTelUpdateModel params = setParams(form);

        /* 属性更新処理 */
        Map<String, String> messageMap = userAndTelSearchService.sharedTelEdit(params);
        String message = StringUtils.isNotEmpty(messageMap.get("errorMessage"))
                ? messageMap.get("errorMessage")
                : messageMap.get("successMessage");
        /* メッセージをセット */
        form.setMessage(message);
        return getJsonView(map);
    }

    /**
     * 共用化名変更
     * 
     * @param form   SharedTelUpdateForm
     * @param result BindingResult
     * @return ModelAndView
     * @throws Exception　例外処理
     */
    @TokenHandler(validate = TokenValidateType.REMOVE)
    @RequestMapping(value = "/sharedTelUpdate", method = RequestMethod.POST)
    public ModelAndView sharedTelUpdate(@Valid SharedTelUpdateForm form, BindingResult result) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        /* 入力チェック */
        if (result.hasErrors()) {
            map.put("errors", result.getAllErrors());
            return getJsonView(map);
        }

        /* Form-Modelコピー処理 */
        UserAndTelUpdateModel params = setParams(form);

        /* 属性更新処理 */
        Map<String, String> messageMap = userAndTelSearchService.sharedTelUpdate(params);
        String message = StringUtils.isNotEmpty(messageMap.get("errorMessage"))
                ? messageMap.get("errorMessage")
                : messageMap.get("successMessage");
        /* メッセージをセット */
        form.setMessage(message);
        return getJsonView(map);
    }

    /**
     * 更新用のFormの値をModelにセットします。
     * 
     * @param form　共有電話機フォーム
     * @return ユーザーと電話機一覧モデル
     */
    private UserAndTelUpdateModel setParams(SharedTelUpdateForm form) {
        /* インスタンス化 */
        UserAndTelUpdateModel params = new UserAndTelUpdateModel();
        params.setSharedUserName(form.getSharedUserName()); // 共用電話名
        params.setUserId(new BigDecimal(form.getUserId()));// ユーザーID
        params.setTelId(new BigDecimal(form.getTelId()));// 電話ID
        params.setLineId(new BigDecimal(form.getLineId()));// ラインID
        params.setCompanyUserId(form.getCompanyUserId()); // 会社ID
        params.setSectionUserId(form.getSectionUserId()); //店部課ID
        params.setSharedUse(form.getSharedUse()); // 共有区分
        return params;
    }
}
