/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.front.entity.FrontPost;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 帖子表Service
 * @author xuyuxiang
 * @version 2018-12-21
 */

public interface FrontPostService extends CrudServiceApi<FrontPost> {

	/**
	 * 获取单条数据
	 * @param frontPost
	 * @return
	 */
	@Override
	public FrontPost get(FrontPost frontPost);
	
	/**
	 * 查询分页数据
	 * @param frontPost 查询条件
	 * @return
	 */
	@Override
	public Page<FrontPost> findPage(FrontPost frontPost);
	
	/**
	 * 保存数据（插入或更新）
	 * @param frontPost
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(FrontPost frontPost);
	
	/**
	 * 更新状态
	 * @param frontPost
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(FrontPost frontPost);
	
	/**
	 * 删除数据
	 * @param frontPost
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(FrontPost frontPost);
	/**
	 * @Author xuyuxiang
	 * @Description 查询该帖子是否有最佳答案
	 * @Date 16:13 2019/1/2
	 * @Param [frontPost]
	 * @return boolean
	 **/
	public boolean hasAccept(FrontPost frontPost);

	/**
	 * @Author xuyuxiang
	 * @Description 根据分类查询本周热议
	 * @Date 13:40 2019/1/4
	 * @Param [frontPost]
	 * @return java.util.List<com.jeesite.modules.front.entity.FrontPost>
	 **/
	public List<FrontPost> findHotPlostList(FrontPost frontPost,int pageSize);
}