package com.jeesite.modules.front.utils;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName FrontUtils
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/20 9:46
 **/
public class FrontUtils {
    /**
     * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
     */
    private static final class Static {
        private static FrontService frontService = SpringUtils.getBean(FrontService.class);
        private static UserService userService = SpringUtils.getBean(UserService.class);
    }
    /**
     * @Author xuyuxiang
     * @Description 判断今天是否签到
     * @Date 9:47 2018/12/20
     * @Param []
     * @return boolean
     **/
    public static boolean isSigned(){
        Front front = Static.frontService.getCurrentFront();
        if(front != null){
            return DateUtils.isSameDay(front.getUpSignDate(),new Date());
        }else{
            return false;
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 判断是否断签
     * @Date 14:17 2018/12/20
     * @Param []
     * @return boolean
     **/
    public static boolean isBreakSign(){
        Front front = Static.frontService.getCurrentFront();
        if(front != null){
            Double spaceDay =  DateUtils.getDistanceOfTwoDate(front.getUpSignDate(),new Date());
            if(spaceDay.intValue() > 1){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 根据签到天数获取今天的飞吻数
     * @Date 11:39 2018/12/20
     * @Param [Count]
     * @return java.lang.Long
     **/
    public static Long getKissTodayBySignCount(Long count) {
        if (count >= 0 && count < 5) {
            return new Long(5);
        }else if(count >=5 && count < 15){
            return new Long(10);
        }else if(count >= 15 && count < 30){
            return new Long(15);
        }else if(count >= 30 && count < 100){
            return new Long(20);
        }else if(count >=100 && count < 365){
            return new Long(30);
        }else{
            return new Long(50);
        }
    }


}
