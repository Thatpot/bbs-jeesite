/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.front.entity.FrontCollect;

/**
 * 收藏表DAO接口
 * @author xuyuxiang
 * @version 2018-12-29
 */
@MyBatisDao
public interface FrontCollectDao extends CrudDao<FrontCollect> {
	
}