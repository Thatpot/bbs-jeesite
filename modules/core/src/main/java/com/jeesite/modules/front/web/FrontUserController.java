/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前台用户表Controller
 * @author xuyuxiang
 * @version 2018-12-18
 */
@Controller
@RequestMapping(value = "${adminPath}/front/frontUser")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
public class FrontUserController extends BaseController {

	@Autowired
	private FrontUserService frontUserService;
	@Autowired
	private UserService userService;
	@Autowired
	private FrontService frontService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public FrontUser get(String userCode, boolean isNewRecord) {
		return frontUserService.get(userCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("front:frontUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(FrontUser frontUser, Model model) {
		model.addAttribute("frontUser", frontUser);
		return "modules/front/frontUserList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("front:frontUser:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<FrontUser> listData(FrontUser frontUser, HttpServletRequest request, HttpServletResponse response) {
		frontUser.setPage(new Page<>(request, response));
		frontUser.getSqlMap().getOrder().setOrderBy("f.create_date DESC");
		Page<FrontUser> page = frontUserService.findPage(frontUser); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("front:frontUser:view")
	@RequestMapping(value = "form")
	public String form(FrontUser frontUser,String op, Model model) {
		model.addAttribute("op", op);
		model.addAttribute("frontUser", frontUser);
		return "modules/front/frontUserForm";
	}

	/**
	 * 保存前台用户表
	 */
	@RequiresPermissions("front:frontUser:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated FrontUser frontUser, String op, HttpServletRequest request) {
		if (User.isSuperAdmin(frontUser.getUserCode())) {
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (!FrontUser.USER_TYPE_FRONT.equals(frontUser.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (StringUtils.inString(op, Global.OP_ADD, Global.OP_EDIT)
				&& UserUtils.getSubject().isPermitted("front:frontUser:edit")){
			frontUserService.save(frontUser);
		}
		return renderResult(Global.TRUE, text("保存用户''{0}''成功", frontUser.getUserName()));
	}

	/**
	 * 停用前台用户表
	 */
	@RequiresPermissions("front:frontUser:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(FrontUser frontUser) {
		frontUser.setStatus(FrontUser.STATUS_DISABLE);
		userService.updateStatus(frontUser);
		Front front = frontUser.getFront();
		front.setStatus(FrontUser.STATUS_DISABLE);
		frontService.updateStatus(front);
		return renderResult(Global.TRUE, text("停用前台用户''{0}''成功", frontUser.getUserName()));
	}
	
	/**
	 * 启用前台用户表
	 */
	@RequiresPermissions("front:frontUser:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(FrontUser frontUser) {
		frontUser.setStatus(FrontUser.STATUS_NORMAL);
		userService.updateStatus(frontUser);
		Front front = frontUser.getFront();
		front.setStatus(FrontUser.STATUS_NORMAL);
		frontService.updateStatus(front);
		return renderResult(Global.TRUE, text("启用前台用户''{0}''成功", frontUser.getUserName()));
	}
	
	/**
	 * 删除前台用户表
	 */
	@RequiresPermissions("front:frontUser:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(FrontUser frontUser) {
		frontUserService.delete(frontUser);
		return renderResult(Global.TRUE, text("删除前台用户表成功！"));
	}

}