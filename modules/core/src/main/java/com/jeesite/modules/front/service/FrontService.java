/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.front.dao.FrontDao;
import com.jeesite.modules.front.entity.Front;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 前台用户表Service
 * @author xuyuxiang
 * @version 2018-12-18
 */
public interface FrontService extends CrudServiceApi<Front> {

    /**
     * 获取单条数据
     * @param front
     * @return
     */
    @Override
    public Front get(Front front);

    /**
     * 查询分页数据
     * @param front 查询条件
     * @return
     */
    @Override
    public Page<Front> findPage(Front front);

    /**
     * 保存数据（插入或更新）
     * @param front
     */
    @Override
    public void save(Front front);

    /**
     * 更新状态
     * @param front
     */
    @Override
    public void updateStatus(Front front);

    /**
     * 删除数据
     * @param front
     */
    @Override
    public void delete(Front front);

    /**
     * @Author xuyuxiang
     * @Description 获取当前用户签到的天数以及今天获得的飞吻数
     * @Date 11:30 2018/12/20
     * @Param
     * @return
     **/
    public Map<String,Long> getCurrentFrontSignCountAndKiss(Front front);
}