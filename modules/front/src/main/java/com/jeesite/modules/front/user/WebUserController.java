package com.jeesite.modules.front.user;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.utils.FrontUtils;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FrontController
 * @Description 前台用户相关操作控制层
 * @Author xuyux
 * @Date 2018/12/5 9:46
 **/
@Controller
@RequestMapping(value = "${frontPath}/front/user")
public class WebUserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private FrontService frontService;
    /**
     * @Author xuyuxiang
     * @Description 用户登录
     * @Date 10:56 2018/12/7
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = ("login"), method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response,Model model) {
        LoginInfo loginInfo = UserUtils.getLoginInfo();
        //如果没登录跳转到登录页
        if(loginInfo == null){
            return "modules/front/user/login";
        }else{//如果登录了跳转到首页
            ServletUtils.redirectUrl(request, response,frontPath+"/front/index");
            return null;
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 用户注册
     * @Date 9:41 2018/12/12
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = ("reg"), method = RequestMethod.GET)
    public String reg(HttpServletRequest request, HttpServletResponse response) {
        LoginInfo loginInfo = UserUtils.getLoginInfo();
        //如果没登录跳转到注册页
        if(loginInfo == null){
            return "modules/front/user/reg";
        }else{//如果登录了跳转到首页
            ServletUtils.redirectUrl(request, response,frontPath+"/front/index");
            return null;
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 用户_个人中心
     * @Date 10:56 2018/12/7
     * @Param [model]
     * @return java.lang.String
     **/
    @RequiresPermissions("front:user:view")
    @RequestMapping(value = ("index"), method = RequestMethod.GET)
    public String index(Model model) {
         model.addAttribute("menuType","index");
         return "modules/front/user/index";
    }
    /**
     * @Author xuyuxiang
     * @Description 用户_基本设置
     * @Date 9:08 2018/12/12
     * @Param [model]
     * @return java.lang.String
     **/
    @RequiresPermissions("front:user:view")
    @RequestMapping(value = ("set"), method = RequestMethod.GET)
    public String set(Model model) {
        model.addAttribute("menuType","set");
        return "modules/front/user/set";
    }
    /**
     * @Author xuyuxiang
     * @Description 用户_我的消息
     * @Date 9:08 2018/12/12
     * @Param [model]
     * @return java.lang.String
     **/
    @RequiresPermissions("front:user:view")
    @RequestMapping(value = ("message"), method = RequestMethod.GET)
    public String message(Model model) {
        model.addAttribute("menuType","message");
        return "modules/front/user/message";
    }
    /**
     * @Author xuyuxiang
     * @Description 用户_我的主页
     * @Date 9:09 2018/12/12
     * @Param [model]
     * @return java.lang.String
     **/
    @RequiresPermissions("front:user:view")
    @RequestMapping(value = ("home"), method = RequestMethod.GET)
    public String home(Model model) {
        return "modules/front/user/home";
    }
    /**
     * @Author xuyuxiang
     * @Description 前台用户修改头像
     * @Date 17:23 2018/12/7
     * @Param [user, request]
     * @return java.lang.String
     **/
    @RequiresPermissions("front:user:view")
    @RequestMapping(value = ("upload"), method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String avatarBase64 = "";
        try {
            String base64Header = "data:"+multipartFile.getContentType()+";base64,";
            String base64footer = EncodeUtils.encodeBase64(multipartFile.getBytes());
            avatarBase64 = base64Header + base64footer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = UserUtils.getUser();
        user.setAvatarBase64(avatarBase64);
        userService.updateUserInfo(user);
        return renderResult("true","上传成功");
    }
    /**
     * @Author xuyuxiang
     * @Description 前台用户修改基本信息
     * @Date 9:07 2018/12/12
     * @Param [user]
     * @return java.lang.String
     **/
    @RequiresPermissions("front:user:view")
    @RequestMapping(value = ("infoSaveBase"), method = RequestMethod.POST)
    @ResponseBody
    public String infoSaveBase(User user) {
        User loginUser = UserUtils.getUser();
        if(loginUser!=null){
            loginUser.setEmail(user.getEmail());
            loginUser.setUserName(user.getUserName());
            loginUser.setSex(user.getSex());
            loginUser.setSign(user.getSign());
        }
        userService.updateUserInfo(loginUser);
        return renderResult("true","修改成功");
    }
    /**
     * @Author xuyuxiang
     * @Description 前台用户签到
     * @Date 9:35 2018/12/20
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = ("sign"), method = RequestMethod.POST)
    @ResponseBody
    public String sign() {
        Front front = frontService.getCurrentFront();
        if(front == null){
            return renderResult("false","请登录");
        }else if(FrontUtils.isSigned()){//判断是否签到过了
            return renderResult("false","今天已签到");
        }else{
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
            frontService.save(front);
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("days",days);
            data.put("signed",true);
            data.put("experience",experience);
            return renderResult("true","签到成功",data);
        }
    }
}