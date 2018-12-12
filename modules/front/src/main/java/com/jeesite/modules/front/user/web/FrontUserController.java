package com.jeesite.modules.front.user.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName FrontController
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/5 9:46
 **/
@Controller
@RequestMapping(value = "${frontPath}/front/user")
public class FrontUserController extends BaseController {
    @Autowired
    private UserService userService;
    /**
     * @Author xuyuxiang
     * @Description 用户登录
     * @Date 10:56 2018/12/7
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = {"login", ""})
    public String login() {
       LoginInfo loginInfo = UserUtils.getLoginInfo();
       //如果没登录跳转到登录页
        if(loginInfo == null){
            return "modules/front/user/login";
        }else{//如果登录了跳转到用户首页
            return "modules/front/user/index";
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 用户注册
     * @Date 9:41 2018/12/12
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = {"reg", ""})
    public String reg() {
        LoginInfo loginInfo = UserUtils.getLoginInfo();
        //如果没登录跳转到注册页
        if(loginInfo == null){
            return "modules/front/user/reg";
        }else{//如果登录了跳转到用户首页
            return "modules/front/user/index";
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
    @RequestMapping(value = {"index", ""})
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
    @RequestMapping(value = {"set", ""})
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
    @RequestMapping(value = {"message", ""})
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
    @RequestMapping(value = {"home", ""})
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
    @RequestMapping(value = {"upload", ""})
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
    @RequestMapping(value = {"infoSaveBase", ""})
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
}
