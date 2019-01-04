package com.jeesite.modules.front.utils;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;

import java.text.SimpleDateFormat;
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
        private static FrontUserService frontUserService = SpringUtils.getBean(FrontUserService.class);
    }
    /**
     * @Author xuyuxiang
     * @Description 判断今天是否签到
     * @Date 9:47 2018/12/20
     * @Param []
     * @return boolean
     **/
    public static boolean isSigned(){
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        if(frontUser != null){
            return DateUtils.isSameDay(frontUser.getFront().getUpSignDate(),new Date());
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
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        if(frontUser != null){
            Double spaceDay =  DateUtils.getDistanceOfTwoDate(frontUser.getFront().getUpSignDate(),new Date());
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

    /**
     * @Author xuyuxiang
     * @Description 获取当前登录的前台用户
     * @Date 12:51 2018/12/21
     * @Param []
     * @return com.jeesite.modules.front.entity.FrontUser
     **/
    public static FrontUser getCurrentFrontUser() {
        User loginUser = UserUtils.getUser();
        FrontUser frontUser = new FrontUser();
        frontUser.setUserCode(loginUser.getUserCode());
        frontUser = Static.frontUserService.get(frontUser);
        return frontUser;
    }
    /**
     * @Author xuyuxiang
     * @Description 获取本周区间，查询本周热议用
     * @Date 14:19 2019/1/4
     * @Param [date]
     * @return java.lang.String
     **/
    public static String getTimeInterval() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String imptimeBegin = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        return imptimeBegin + "~" + imptimeEnd;
    }
}
