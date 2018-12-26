/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service.support;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontDao;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.utils.FrontUtils;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 前台用户表Service
 * @author xuyuxiang
 * @version 2018-12-18
 */
@Service
@Transactional(readOnly=true)
public class FrontServiceSupport extends CrudService<FrontDao, Front> implements FrontService {

    /**
     * 获取单条数据
     * @param front
     * @return
     */
    @Override
    public Front get(Front front) {
        return super.get(front);
    }

    /**
     * 查询分页数据
     * @param front 查询条件
     * @return
     */
    @Override
    public Page<Front> findPage(Front front) {
        return super.findPage(front);
    }

    /**
     * 保存数据（插入或更新）
     * @param front
     */
    @Override
    @Transactional(readOnly=false)
    public void save(Front front) {
        super.save(front);
    }

    /**
     * 更新状态
     * @param front
     */
    @Override
    @Transactional(readOnly=false)
    public void updateStatus(Front front) {
        super.updateStatus(front);
    }

    /**
     * 删除数据
     * @param front
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(Front front) {
        super.delete(front);
    }

    /**
     * @Author xuyuxiang
     * @Description 根据用户获取签到天数和今天获得的飞吻数
     * @Date 14:33 2018/12/20
     * @Param [front]
     * @return java.util.Map<java.lang.String,java.lang.Long>
     **/
    @Override
    public Map<String, Long> getCurrentFrontSignCountAndKiss(Front front) {
        Map<String,Long> map = new HashMap<String,Long>();
        Long days = new Long(0);
        //没断签
        if(!FrontUtils.isBreakSign()){
            days =  front.getUpSignCount();
        }
        Long experience = FrontUtils.getKissTodayBySignCount(days);
        Long experienceShould = FrontUtils.getKissTodayBySignCount(days + 1);
        //连续签到天数
        map.put("days",days);
        //今天已签到获得的飞吻数
        map.put("experience",experience);
        //今天签到可以获得的飞吻数
        map.put("experienceShould",experienceShould);
        return map;
    }


}