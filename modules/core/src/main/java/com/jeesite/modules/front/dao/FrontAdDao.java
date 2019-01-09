/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.front.entity.FrontAd;

/**
 * 广告表DAO接口
 * @author xuyuxiang
 * @version 2019-01-09
 */
@MyBatisDao
public interface FrontAdDao extends CrudDao<FrontAd> {
	
}