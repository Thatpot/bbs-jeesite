/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.front.dao.FrontUserDao;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.sys.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 前台用户表Service
 * @author xuyuxiang
 * @version 2018-12-18
 */
public interface FrontUserService extends CrudServiceApi<FrontUser> {
	
	/**
	 * 获取单条数据
	 * @param frontUser
	 * @return
	 */
	@Override
	public FrontUser get(FrontUser frontUser);
	
	/**
	 * 查询分页数据
	 * @param frontUser 查询条件
	 * @return
	 */
	@Override
	public Page<FrontUser> findPage(FrontUser frontUser);
	
	/**
	 * 保存数据（插入或更新）
	 * @param frontUser
	 */
	@Override
	public void save(FrontUser frontUser);

	/**
	 * 更新状态
	 * @param frontUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(FrontUser frontUser);
	
	/**
	 * 删除数据
	 * @param frontUser
	 */
	@Override
	public void delete(FrontUser frontUser);

	/**
	 * @Author xuyuxiang
	 * @Description 签到活跃榜
	 * @Date 17:53 2018/12/20
	 * @Param []
	 * @return java.util.List<java.util.List<com.jeesite.modules.front.entity.FrontUser>>
	 **/
	public List<List<FrontUser>> signin();
	/**
	 * @Author xuyuxiang
	 * @Description 获取用户签到数据
	 * @Date 17:55 2018/12/20
	 * @Param
	 * @return
	 **/
	public Map<String,Object> sign(Front front);

	/**
	 * @Author xuyuxiang
	 * @Description 根据用户获取签到天数和今天获得的飞吻数
	 * @Date 14:33 2018/12/20
	 * @Param [front]
	 * @return java.util.Map<java.lang.String,java.lang.Long>
	 **/
	Map<String, Long> getCurrentFrontSignCountAndKiss(FrontUser frontUser);

	/**
	 * @Author xuyuxiang
	 * @Description 根据字段查询model
	 * @Date 17:12 2019/1/2
	 * @Param [frontUser]
	 * @return com.jeesite.modules.front.entity.FrontUser
	 **/
	public FrontUser getByEntity(FrontUser frontUser);

	/**
	 * @Author xuyuxiang
	 * @Description 用户注册时候分配角色
	 * @Date 16:47 2019/1/8
	 * @Param [frontUser]
	 * @return void
	 **/
	public void saveAuth(User user);
}