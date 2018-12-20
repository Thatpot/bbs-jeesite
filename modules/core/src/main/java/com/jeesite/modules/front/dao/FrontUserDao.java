/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.front.entity.FrontUser;

/**
 * 前台用户表DAO接口
 * @author xuyuxiang
 * @version 2018-12-18
 */
@MyBatisDao(dataSourceName= DataSourceHolder.DEFAULT)
public interface FrontUserDao extends CrudDao<FrontUser> {
	
}