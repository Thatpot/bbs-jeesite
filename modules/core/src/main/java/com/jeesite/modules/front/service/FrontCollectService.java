/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service;

import java.util.List;

import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.front.entity.Front;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.entity.FrontCollect;
import com.jeesite.modules.front.dao.FrontCollectDao;

/**
 * 收藏表Service
 * @author xuyuxiang
 * @version 2018-12-29
 */
public interface FrontCollectService extends CrudServiceApi<FrontCollect> {
	
	/**
	 * 获取单条数据
	 * @param frontCollect
	 * @return
	 */
	@Override
	public FrontCollect get(FrontCollect frontCollect);

	/**
	 * @Author xuyuxiang
	 * @Description 根据实体获取数据
	 * @Date 17:32 2018/12/29
	 * @Param [frontCollect]
	 * @return com.jeesite.modules.front.entity.FrontCollect
	 **/
	public FrontCollect getByEntity(FrontCollect frontCollect);


	/**
	 * 查询分页数据
	 * @param frontCollect 查询条件
	 * @return
	 */
	@Override
	public Page<FrontCollect> findPage(FrontCollect frontCollect);

	/**
	 * 保存数据（插入或更新）
	 * @param frontCollect
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(FrontCollect frontCollect);

	/**
	 * 删除数据
	 * @param frontCollect
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(FrontCollect frontCollect);
	
}