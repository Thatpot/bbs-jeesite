/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.front.entity.FrontPost;

/**
 * 帖子表DAO接口
 * @author xuyuxiang
 * @version 2018-12-21
 */
@MyBatisDao
public interface FrontPostDao extends CrudDao<FrontPost> {
	
}