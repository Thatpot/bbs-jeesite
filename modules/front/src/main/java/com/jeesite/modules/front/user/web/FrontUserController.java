package com.jeesite.modules.front.user.web;

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
     * @Description 用户个人中心
     * @Date 10:56 2018/12/7
     * @Param [model]
     * @return java.lang.String
     **/
    @RequiresPermissions("front:user:view")
    @RequestMapping(value = {"index", ""})
    public String index(Model model) {
         return "modules/front/user/index";
    }

    @RequiresPermissions("front:user:view")
    @RequestMapping(value = {"set", ""})
    public String set(Model model) {
        return "modules/front/user/set";
    }

    @RequiresPermissions("front:user:view")
    @RequestMapping(value = {"message", ""})
    public String message(Model model) {
        return "modules/front/user/message";
    }

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
    public void upload(String avatarBase64) {

        User user = UserUtils.getUser();
        user.setAvatarBase64(avatarBase64);
        userService.updateUserInfo(user);
        System.out.println(avatarBase64);
    }
}
