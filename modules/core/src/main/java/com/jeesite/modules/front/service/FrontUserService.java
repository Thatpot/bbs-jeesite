/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.front.dao.FrontUserDao;
import com.jeesite.modules.front.entity.FrontUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
}