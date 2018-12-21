/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.service.FrontPostService;

/**
 * 帖子表Controller
 * @author xuyuxiang
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/front/frontPost")
public class FrontPostController extends BaseController {

	@Autowired
	private FrontPostService frontPostService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public FrontPost get(String id, boolean isNewRecord) {
		return frontPostService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("front:frontPost:view")
	@RequestMapping(value = {"list", ""})
	public String list(FrontPost frontPost, Model model) {
		model.addAttribute("frontPost", frontPost);
		return "modules/front/frontPostList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("front:frontPost:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<FrontPost> listData(FrontPost frontPost, HttpServletRequest request, HttpServletResponse response) {
		frontPost.setPage(new Page<>(request, response));
		Page<FrontPost> page = frontPostService.findPage(frontPost); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("front:frontPost:view")
	@RequestMapping(value = "form")
	public String form(FrontPost frontPost, Model model) {
		model.addAttribute("frontPost", frontPost);
		return "modules/front/frontPostForm";
	}

	/**
	 * 保存帖子表
	 */
	@RequiresPermissions("front:frontPost:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated FrontPost frontPost) {
		frontPostService.save(frontPost);
		return renderResult(Global.TRUE, text("保存帖子表成功！"));
	}
	
	/**
	 * 删除帖子表
	 */
	@RequiresPermissions("front:frontPost:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(FrontPost frontPost) {
		frontPostService.delete(frontPost);
		return renderResult(Global.TRUE, text("删除帖子表成功！"));
	}
	
}