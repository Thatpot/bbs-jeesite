/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service;

import java.util.List;

import com.jeesite.common.service.api.CrudServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.entity.FrontAd;
import com.jeesite.modules.front.dao.FrontAdDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 广告表Service
 * @author xuyuxiang
 * @version 2019-01-09
 */
@Service
@Transactional(readOnly=true)
public interface FrontAdService extends CrudServiceApi<FrontAd> {
	
	/**
	 * 获取单条数据
	 * @param frontAd
	 * @return
	 */
	@Override
	public FrontAd get(FrontAd frontAd);
	
	/**
	 * 查询分页数据
	 * @param frontAd 查询条件
	 * @return
	 */
	@Override
	public Page<FrontAd> findPage(FrontAd frontAd);
	
	/**
	 * 保存数据（插入或更新）
	 * @param frontAd
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(FrontAd frontAd);
	
	/**
	 * 更新状态
	 * @param frontAd
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(FrontAd frontAd);
	
	/**
	 * 删除数据
	 * @param frontAd
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(FrontAd frontAd);
	
}