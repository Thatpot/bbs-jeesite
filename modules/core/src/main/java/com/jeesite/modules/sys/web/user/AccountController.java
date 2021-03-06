/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.user;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.msg.EmailUtils;
import com.jeesite.common.msg.SmsUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.PwdUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.utils.ValidCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账号自助服务Controller
 * @author ThinkGem
 * @version 2017-12-7
 */
@Controller
@RequestMapping(value = "/account")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
public class AccountController extends BaseController{

	@Autowired
	private UserService userService;
	@Autowired
	private FrontService frontService;
	@Autowired
	private FrontUserService frontUserService;

	/**
	 * 忘记密码页面
	 */
	@RequestMapping(value = "forgetPwd")
	public String forgetPwd(Model model) {
		model.addAttribute("message", "正在研发中，敬请期待！");
		return "modules/sys/account/forgetPwd";
	}
	
	/**
	 * 获取找回密码短信、邮件验证码
	 * @param validCode 图片验证码，防止重复机器人。
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getFpValidCode")
	@ResponseBody
	public String getFpValidCode(User user, String validCode, String validType, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, "图片验证码不正确或已失效，请点击图片刷新！");
		}
		if (!"mobile".equals(validType) && !"email".equals(validType)){
			return renderResult(Global.FALSE, "非法操作。");
		}
		User u = UserUtils.getByLoginCode(user.getLoginCode());
		if(u == null){
			return renderResult(Global.FALSE, "登录账号不正确！");
		}
		if("mobile".equals(validType) && StringUtils.isBlank(u.getMobile())){
			return renderResult(Global.FALSE, "该账号未设置手机号码！");
		}else if("email".equals(validType) && StringUtils.isBlank(u.getEmail())){
			return renderResult(Global.FALSE, "该账号未设置邮件地址！");
		}
		// 操作是否频繁验证， 如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("fpLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, "您当前操作太频繁，请稍等一会再操作！");
		}else{
			UserUtils.putCache("fpLastDate", new Date());
		}
		// 生成验证码，并缓存。
		String fpValidCode = StringUtils.getRandomNum(6);
		UserUtils.putCache("fpUserCode", u.getUserCode());
		UserUtils.putCache("fpLoginCode", u.getLoginCode());
		UserUtils.putCache("fpValidCode", fpValidCode);
		// 发送短信消息。
		if("mobile".equals(validType)){
			return sendSmsValidCode(u, fpValidCode, "找回密码");
		}
		// 发送邮件消息。
		else if("email".equals(validType)){
			return sendEmailValidCode(u, fpValidCode, "找回密码");
		}
		return null;
	}
	
	/**
	 * 根据短信或邮件验证码保存密码
	 */
	@PostMapping(value = "savePwdByValidCode")
	@ResponseBody
	public String savePwdByValidCode(User user, String fpValidCode, HttpServletRequest request) {
		String userCode = (String)UserUtils.getCache("fpUserCode");
		String loginCode = (String)UserUtils.getCache("fpLoginCode");
		String validCode = (String)UserUtils.getCache("fpValidCode");
		Date date = (Date)UserUtils.getCache("fpLastDate");
		
		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(userCode != null && loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, "请重新获取验证码！");
		}
		
		// 清理验证码，验证码只允许使用一次。
		UserUtils.removeCache("fpUserCode");
		UserUtils.removeCache("fpLoginCode");
		UserUtils.removeCache("fpValidCode");
		UserUtils.removeCache("fpLastDate");
		
		// 验证码是否超时
		boolean isTimeout = true;
		String validTime = Global.getConfig("sys.account.validCodeTimeout", "10"); //验证码有效时间（单位分钟，0表示不限制，默认值10）
		if("0".equals(validTime) || (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 60*Long.parseLong(validTime))){
			isTimeout = false;
		}
		if (!(validCode != null && validCode.equals(fpValidCode) && !isTimeout)){
			return renderResult(Global.FALSE, "验证码不正确或已失效，请重新获取验证码！");
		}
		
		// 更新为新密码。
		try{
			userService.updatePassword(userCode, user.getPassword());
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		return renderResult(Global.TRUE, "恭喜你，您的账号 "+loginCode+" 密码修改成功！");
	}

	/**
	 * 获取保密问题
	 * @param validCode 图片验证码，防止重复机器人。
	 */
	@PostMapping(value = "getPwdQuestion")
	@ResponseBody
	public String getPwdQuestion(User user, String validCode, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, "图片验证码不正确或已失效，请点击图片刷新！");
		}
		// 账号是否存在验证
		User u = UserUtils.getByLoginCode(user.getLoginCode());
		if (u == null){
			return renderResult(Global.FALSE, "登录账号不正确！");
		}
		// 操作是否频繁验证， 如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("fpLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, "您当前操作太频繁，请稍等一会再操作！");
		}else{
			UserUtils.putCache("fpLastDate", new Date());
		}

		// 未设置密保
		if (StringUtils.isAnyBlank(u.getPwdQuestion(), u.getPwdQuestion2(), u.getPwdQuestion3())){
			return renderResult(Global.FALSE, "该账号未设置密保问题！");
		}
		
		// 获取保密问题，并缓存
		Map<String, String> data = MapUtils.newHashMap();
		data.put("pwdQuestion", u.getPwdQuestion());
		data.put("pwdQuestion2", u.getPwdQuestion2());
		data.put("pwdQuestion3", u.getPwdQuestion3());
		UserUtils.putCache("fpUserCode", u.getUserCode());
		UserUtils.putCache("fpLoginCode", u.getLoginCode());
		return renderResult(Global.TRUE, "获取密保问题成功！", data);
	}

	/**
	 * 校验密保问题答案
	 * @param user
	 * @param request
	 * @return
	 */
	@PostMapping(value = "savePwdByPwdQuestion")
	@ResponseBody
	public String savePwdByPwdQuestion(User user, HttpServletRequest request) {
		String userCode = (String)UserUtils.getCache("fpUserCode");
		String loginCode = (String)UserUtils.getCache("fpLoginCode");
		
		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(userCode != null && loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, "请重新获取保密问题！");
		}
		
		// 清理保密问题，每次获取只允许使用一次。
		UserUtils.removeCache("fpUserCode");
		UserUtils.removeCache("fpLoginCode");
		
		// 验证三个密保问题是否正确。
		User u = UserUtils.getByLoginCode(user.getLoginCode());
		if (!(u != null && loginCode.equals(user.getLoginCode())
				&& PwdUtils.validatePassword(user.getPwdQuestionAnswer(), u.getPwdQuestionAnswer())
				&& PwdUtils.validatePassword(user.getPwdQuestionAnswer2(), u.getPwdQuestionAnswer2())
				&& PwdUtils.validatePassword(user.getPwdQuestionAnswer3(), u.getPwdQuestionAnswer3()))) {
			return renderResult(Global.FALSE, "您填写的密保问题答案不正确！");
		}
		
		// 更新为新密码。
		try{
			userService.updatePassword(userCode, user.getPassword());
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		return renderResult(Global.TRUE, "验证通过");
	}
	
	/**
	 * 用户注册页面
	 * @param user 用户信息参数
	 */
	@RequestMapping(value = "registerUser")
	public String registerUser(User user, HttpServletRequest request) {
		return "modules/sys/account/registerUser";
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getRegValidCode")
	@ResponseBody
	public String getRegValidCode(User user, String validCode, String validType, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, "图片验证码不正确或已失效，请点击图片刷新！");
		}
		if (!"mobile".equals(validType) && !"email".equals(validType)){
			return renderResult(Global.FALSE, "非法操作。");
		}
		// 非空数据校验。
		if (!(StringUtils.isNotBlank(user.getLoginCode()) && StringUtils.isNotBlank(user.getUserName()))){
			return renderResult(Global.FALSE, "登录账号和用户姓名不能为空！");
		}
		// 邮箱、手机号码是否填写验证
		if("email".equals(validType) && !StringUtils.isNotBlank(user.getEmail())){
			return renderResult(Global.FALSE, "电子邮箱不能为空！");
		}
		else if("mobile".equals(validType) && !StringUtils.isNotBlank(user.getMobile())){
			return renderResult(Global.FALSE, "手机号码不能为空！");
		}
		// 操作是否频繁验证，如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("regLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, "您当前操作太频繁，请稍等一会再操作！");
		}else{
			UserUtils.putCache("regLastDate", new Date());
		}
		// 验证用户编码是否存在。
		if (UserUtils.getByLoginCode(user.getLoginCode()) != null){
			return renderResult(Global.FALSE, "登录账号已存在！");
		}
		// 生成验证码，并缓存。
		String code = StringUtils.getRandomNum(6);
		UserUtils.putCache("regCorpCode", user.getCorpCode());
		UserUtils.putCache("regCorpName", user.getCorpName());
		UserUtils.putCache("regLoginCode", user.getLoginCode());
		// 账号注册类型
		String[] userTypes = StringUtils.split(Global.getConfig(
				"sys.account.registerUser.userTypes", "-1"), ",");
		if (StringUtils.inString(user.getUserType(), userTypes)){
			UserUtils.putCache("regUserType", user.getUserType());
		}else{
			return renderResult(Global.FALSE, "非法的用户类型！");
		}
		UserUtils.putCache("regEmail", user.getEmail());
		//UserUtils.putCache("regMobile", user.getMobile());//TODO
		UserUtils.putCache("regValidCode", code);

		//设置默认的用户角色
        UserUtils.putCache("regRoleString",Global.getConfig(
                "sys.user.defaultRoleCodes.front", "front"));
		// 发送邮箱或短信验证码
		if("email".equals(validType)){
			return sendEmailValidCode(user, code, "注册账号");
		}else if("mobile".equals(validType)){
			return sendSmsValidCode(user, code, "注册账号");
		}
		return null;
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param regValidCode 验证码
	 */
	@PostMapping(value = "saveRegByValidCode")
	@ResponseBody
	public String saveRegByValidCode(User user, String regValidCode, HttpServletRequest request) {
		if (!"true".equals(Global.getConfig("sys.account.registerUser"))){
			return renderResult(Global.FALSE, "当前系统没有开启注册功能！");
		}
		String corpCode = (String)UserUtils.getCache("regCorpCode");
		String corpName = (String)UserUtils.getCache("regCorpName");
		String userType = (String)UserUtils.getCache("regUserType");
		String loginCode = (String)UserUtils.getCache("regLoginCode");
		String email = (String)UserUtils.getCache("regEmail");
		String mobile = (String)UserUtils.getCache("regMobile");
		String validCode = (String)UserUtils.getCache("regValidCode");
		Date date = (Date)UserUtils.getCache("regLastDate");
		//设置默认角色
		String roleString = (String)UserUtils.getCache("regRoleString");

		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, "非法操作。");
		}
		
		// 验证码是否超时
		boolean isTimeout = true;
		String validTime = Global.getConfig("sys.account.validCodeTimeout", "10"); //验证码有效时间（单位分钟，0表示不限制，默认值10）
		if("0".equals(validTime) || (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 60*Long.parseLong(validTime))){
			isTimeout = false;
		}
		if (!(validCode != null && validCode.equals(regValidCode) && !isTimeout)){
			return renderResult(Global.FALSE, "验证码不正确或已失效，请重新获取验证码！");
		}

		// 非空数据校验。
		if (!(StringUtils.isNotBlank(user.getLoginCode()) && StringUtils.isNotBlank(user.getUserName()))){
			return renderResult(Global.FALSE, "登录账号和用户姓名不能为空！");
		}
		
		// 新增并保存用户。
		User u = new User();
		u.setIsNewRecord(true);
		if (StringUtils.isNotBlank(corpCode)){
			u.setCorpCode(corpCode);
			u.setCorpName(corpName);
		}
		u.setLoginCode(loginCode);
		u.setUserName(user.getUserName());
		u.setPassword(user.getPassword());
		u.setEmail(email);
		u.setMobile(mobile);
		u.setUserType(userType);
		u.setMgrType(User.MGR_TYPE_NOT_ADMIN);
		userService.genId(u, u.getLoginCode());
		u.setUserCode(u.getUserCode()+"_"+ IdGen.randomBase62(4).toLowerCase());

		//保存完成之后往前台用户表保存数据
		Front front = new Front();
		front.setIsNewRecord(true);

		front.setUpCode(u.getUserCode());
		front.setUpName(u.getUserName());
		front.setUpKiss(new Long(10));
		front.setUpSignCount(new Long(0));
		front.setUpSignDate(new Date(0));
		front.setUpWealth(new Long(0));
		front.setUpCommentCount(new Long(0));
		frontService.save(front);
		u.setRefCode(front.getUpCode());
		u.setRefName(front.getUpName());
		userService.save(u);
		//给用户分配前台角色
		u.setUserRoleString(roleString+",");
		frontUserService.saveAuth(u);

		// 验证成功后清理验证码，验证码只允许使用一次。
		UserUtils.removeCache("regUserType");
		UserUtils.removeCache("regLoginCode");
		UserUtils.removeCache("regValidCode");
		UserUtils.removeCache("regLastDate");
	
		return renderResult(Global.TRUE, "恭喜你，您的账号 "+u.getLoginCode()+" 注册成功！");
	}
	
	/**
	 * 发送邮件验证码
	 */
	private String sendEmailValidCode(User user, String code, String title){
		String account = user.getEmail();
		try {
			title = user.getUserName() + "（" + user.getLoginCode() + "）"+title+"验证码";
			String content = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','微软雅黑','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; text-decoration: none;\" href=\"http://fly.layui.com/\" target=\"_blank\" rel=\"noopener\">Fly社区 - Layui前端框架官方社区</a></div> </td> </tr> <tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> Hi，<span style=\"font-weight: 700;\">" +
					user.getUserName() +
					"</span>，您的邮箱验证码为:<br /><div style=\"font-weight: 700;font-size: 16px;margin-top: 10px;color:#5EB576\">" +
					code +
					"</div><br />请复制后，填写在你的验证码窗口完成验证。 </p>  <p style=\"line-height: 20px; margin-top: 20px; padding: 10px; background-color: #f2f2f2; font-size: 12px;\"> 如果该邮件不是由你本人操作，请忽略。 </p> </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">系统邮件，请勿直接回复。</td> </tr> </tbody> </table>";
//			MsgPushUtils.push(MsgPush.TYPE_EMAIL, title, content, null, null, receiveUserCode);
			EmailUtils.send(account, title, content);
		} catch (Exception e) {
			logger.error(title+"发送邮件错误。", e);
			return renderResult(Global.FALSE, "系统出现了点问题，错误信息：" + e.getMessage());
		}
		account = account.replaceAll("([\\w\\W]?)([\\w\\W]+)([\\w\\W])(@[\\w\\W]+)", "$1****$3$4");
		return renderResult(Global.TRUE, "验证码已发送到“"+account+"”邮箱账号，请尽快查收！");
	}
	
	/**
	 * 发送短信验证码
	 */
	private String sendSmsValidCode(User user, String code, String title){
		String account = user.getMobile();
		try {
			title = user.getUserName() + "（" + user.getLoginCode() + "）"+title+"验证码";
			String content = "您好，您的验证码是：" + code +"（请勿透露给其他人）感谢您的使用。";
//			String receiveUserCode = "[CODE]"+account;
//			MsgPushUtils.push(MsgPush.TYPE_SMS, title, content, null, null, receiveUserCode);
			SmsUtils.send(content, account);
		} catch (Exception e) {
			logger.error(title+"发送短信错误。", e);
			return renderResult(Global.FALSE, "系统出现了点问题，错误信息：" + e.getMessage());
		}
		account = account.replaceAll("(\\d{3})(\\d+)(\\d{3})","$1****$3");
		return renderResult(Global.TRUE, "验证码已发送到“"+account+"”的手机号码，请尽快查收！");
	}
	
}
