/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.web.http.ServletUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.front.entity.FrontAd;
import com.jeesite.modules.front.service.FrontAdService;

/**
 * 广告表Controller
 * @author xuyuxiang
 * @version 2019-01-09
 */
@Controller
@RequestMapping(value = "${adminPath}/front/frontAd")
public class FrontAdController extends BaseController {

	@Autowired
	private FrontAdService frontAdService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public FrontAd get(String id, boolean isNewRecord) {
		return frontAdService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("front:frontAd:view")
	@RequestMapping(value = {"list", ""})
	public String list(FrontAd frontAd, Model model) {
		model.addAttribute("frontAd", frontAd);
		return "modules/front/frontAdList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("front:frontAd:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<FrontAd> listData(FrontAd frontAd, HttpServletRequest request, HttpServletResponse response) {
		frontAd.setPage(new Page<>(request, response));
		Page<FrontAd> page = frontAdService.findPage(frontAd); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("front:frontAd:view")
	@RequestMapping(value = "form")
	public String form(FrontAd frontAd, Model model) {
		model.addAttribute("frontAd", frontAd);
		return "modules/front/frontAdForm";
	}

	/**
	 * 保存广告表
	 */
	@RequiresPermissions("front:frontAd:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated FrontAd frontAd) {
		System.out.println(ServletUtils.getParameter("frontAd_image"));
		frontAdService.save(frontAd);
		return renderResult(Global.TRUE, text("保存广告表成功！"));
	}
	
	/**
	 * 停用广告表
	 */
	@RequiresPermissions("front:frontAd:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(FrontAd frontAd) {
		frontAd.setStatus(FrontAd.STATUS_DISABLE);
		frontAdService.updateStatus(frontAd);
		return renderResult(Global.TRUE, text("停用广告表成功"));
	}
	
	/**
	 * 启用广告表
	 */
	@RequiresPermissions("front:frontAd:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(FrontAd frontAd) {
		frontAd.setStatus(FrontAd.STATUS_NORMAL);
		frontAdService.updateStatus(frontAd);
		return renderResult(Global.TRUE, text("启用广告表成功"));
	}
	
}