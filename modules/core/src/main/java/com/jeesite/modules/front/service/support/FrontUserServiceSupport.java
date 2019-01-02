/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.front.service.support;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.front.dao.FrontDao;
import com.jeesite.modules.front.dao.FrontUserDao;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.front.utils.FrontUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台用户表Service
 * @author xuyuxiang
 * @version 2018-12-18
 */
@Service
@Transactional(readOnly=true)
public class FrontUserServiceSupport extends CrudService<FrontUserDao, FrontUser> implements FrontUserService {
    @Autowired
    private FrontDao frontDao;
    @Autowired
    private FrontUserDao frontUserDao;
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
        Front front = frontUser.getFront();
        if(front.getIsNewRecord()){
            frontDao.insert(front);
        }else{
            frontDao.update(front);
        }
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

    @Override
    public List<List<FrontUser>> signin() {
        List<List<FrontUser>> list = ListUtils.newArrayList();

        FrontUser frontUser = new FrontUser();
        frontUser.setPageSize(20);
        frontUser.getSqlMap().put("extkeys","AND f.up_sign_count != 0 AND TO_DAYS(f.up_sign_date) = TO_DAYS(now())");
        frontUser.getSqlMap().getOrder().setOrderBy("f.up_sign_date DESC");
        List<FrontUser> newlist = this.findPage(frontUser).getList();

        frontUser = new FrontUser();
        frontUser.setPageSize(20);
        frontUser.getSqlMap().put("extkeys","AND f.up_sign_count != 0 AND TO_DAYS(f.up_sign_date) = TO_DAYS(now())");
        frontUser.getSqlMap().getOrder().setOrderBy("f.up_sign_date ASC");
        List<FrontUser> fastlist = this.findPage(frontUser).getList();

        frontUser = new FrontUser();
        frontUser.setPageSize(20);
        frontUser.getSqlMap().put("extkeys","AND f.up_sign_count != 0");
        frontUser.getSqlMap().getOrder().setOrderBy("f.up_sign_count DESC");
        List<FrontUser> totlalist = this.findPage(frontUser).getList();

        list.add(newlist);
        list.add(fastlist);
        list.add(totlalist);
        return list;
    }
    /**
     * @Author xuyuxiang
     * @Description 获取用户签到数据
     * @Date 17:55 2018/12/20
     * @Param
     * @return
     **/
    @Override
    @Transactional(readOnly=false)
    public Map<String, Object> sign(Front front) {
        front.setUpSignDate(new Date());
        //判断是否断签
        if(FrontUtils.isBreakSign()){
            //断签从1开始计算
            front.setUpSignCount(new Long(1));
        }else{
            //未断签天数加1
            front.setUpSignCount(front.getUpSignCount() + new Long(1));
        }
        Long days = front.getUpSignCount();
        Long experience = FrontUtils.getKissTodayBySignCount(days);
        front.setUpKiss(front.getUpKiss() + experience);
        frontDao.update(front);
        Map<String,Object> data = MapUtils.newHashMap();
        data.put("days",days);
        data.put("signed",true);
        data.put("experience",experience);
        return data;
    }

    /**
     * @Author xuyuxiang
     * @Description 根据用户获取签到天数和今天获得的飞吻数
     * @Date 14:33 2018/12/20
     * @Param [front]
     * @return java.util.Map<java.lang.String,java.lang.Long>
     **/
    @Override
    public Map<String, Long> getCurrentFrontSignCountAndKiss(FrontUser frontUser) {
        Map<String,Long> map = new HashMap<String,Long>();
        Long days = new Long(0);
        //没断签
        if(!FrontUtils.isBreakSign()){
            days =  frontUser.getFront().getUpSignCount();
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
    @Override
    public FrontUser getByEntity(FrontUser frontUser) {
        return frontUserDao.getByEntity(frontUser);
    }

}