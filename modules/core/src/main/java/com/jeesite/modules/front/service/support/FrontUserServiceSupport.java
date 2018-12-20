/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service.support;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontUserDao;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.service.FrontUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 前台用户表Service
 * @author xuyuxiang
 * @version 2018-12-18
 */
@Service
@Transactional(readOnly=true)
public class FrontUserServiceSupport extends CrudService<FrontUserDao, FrontUser> implements FrontUserService {
    @Autowired
    private FrontService frontService;
    /**
     * 获取单条数据
     * @param frontUser
     * @return
     */
    @Override
    public FrontUser get(FrontUser frontUser) {
        return super.get(frontUser);
    }

    /**
     * 查询分页数据
     * @param frontUser 查询条件
     * @return
     */
    @Override
    public Page<FrontUser> findPage(FrontUser frontUser) {
        return super.findPage(frontUser);
    }

    /**
     * 保存数据（插入或更新）
     * @param frontUser
     */
    @Override
    @Transactional(readOnly=false)
    public void save(FrontUser frontUser) {
        super.save(frontUser);
        frontService.save(frontUser.getFront());

    }

    /**
     * 更新状态
     * @param frontUser
     */
    @Override
    @Transactional(readOnly=false)
    public void updateStatus(FrontUser frontUser) {
        super.updateStatus(frontUser);
    }

    /**
     * 删除数据
     * @param frontUser
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(FrontUser frontUser) {
        super.delete(frontUser);
    }

}