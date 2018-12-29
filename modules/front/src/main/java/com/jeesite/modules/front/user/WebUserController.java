package com.jeesite.modules.front.user;

import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.front.entity.FrontCollect;
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontCollectService;
import com.jeesite.modules.front.service.FrontPostService;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.front.utils.FrontUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlElementDecl;
import java.io.IOException;

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
    private FrontUserService frontUserService;
    @Autowired
    private FrontPostService frontPostService;
    @Autowired
    private FrontCollectService frontCollectService;
    /**
     * @Author xuyuxiang
     * @Description 获取用户
     * @Date 11:53 2018/12/21
     * @Param [userCode, isNewRecord]
     * @return com.jeesite.modules.front.entity.FrontUser
     **/
    @ModelAttribute
    public FrontUser get(String userCode, boolean isNewRecord) {
        return frontUserService.get(userCode, isNewRecord);
    }
    /**
     * @Author xuyuxiang
     * @Description 用户登录
     * @Date 10:56 2018/12/7
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = ("login"), method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response,Model model) {
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        //如果没登录跳转到登录页
        if(frontUser == null){
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
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        //如果没登录跳转到注册页
        if(frontUser == null){
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
    @RequestMapping(value = ("index"), method = RequestMethod.GET)
    public String index(Model model) {
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        model.addAttribute("menuType","index");
        model.addAttribute("frontUser",frontUser);
        model.addAttribute("map", frontUserService.getCurrentFrontSignCountAndKiss(frontUser));
        return "modules/front/user/index";
    }
    /**
     * @Author xuyuxiang
     * @Description 用户_基本设置
     * @Date 9:08 2018/12/12
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("set"), method = RequestMethod.GET)
    public String set(Model model) {
        model.addAttribute("menuType","set");
        model.addAttribute("frontUser",FrontUtils.getCurrentFrontUser());
        return "modules/front/user/set";
    }
    /**
     * @Author xuyuxiang
     * @Description 我的帖子页面
     * @Date 17:11 2018/12/28
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("post"), method = RequestMethod.GET)
    public String post(Model model) {
        model.addAttribute("menuType","post");
        return "modules/front/user/post";
    }

    /**
     * @Author xuyuxiang
     * @Description 我的帖子数据接口
     * @Date 9:57 2018/12/29
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = ("postapi"), method = RequestMethod.POST)
    @ResponseBody
    public String apipost(HttpServletRequest request, HttpServletResponse response){
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        String userCode = frontUser.getUserCode();
        FrontPost frontPost = new FrontPost();
        frontPost.getSqlMap().getWhere().disableAutoAddStatusWhere();
        frontPost.setStatus("");
        frontPost.setCreateBy(userCode);
        frontPost.setPage(new Page<>(request, response));
        Page<FrontPost> pageData = frontPostService.findPage(frontPost);
        return renderResult("true","查询成功",pageData);
    }
    /**
     * @Author xuyuxiang
     * @Description 我收藏的帖子数据入口
     * @Date 17:51 2018/12/29
     * @Param [request, response]
     * @return com.jeesite.common.entity.Page<com.jeesite.modules.front.entity.FrontCollect>
     **/
    @RequestMapping(value = ("collectapi"), method = RequestMethod.POST)
    @ResponseBody
    public String collectapi(HttpServletRequest request, HttpServletResponse response){
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        FrontCollect frontCollect = new FrontCollect();
        frontCollect.setUserCode(frontUser.getUserCode());
        frontCollect.setPage(new Page<>(request, response));
        Page<FrontCollect> pageData = frontCollectService.findPage(frontCollect);
        return renderResult("true","查询成功",pageData);
    }



    /**
     * @Author xuyuxiang
     * @Description 用户_我的消息
     * @Date 9:08 2018/12/12
     * @Param [model]
     * @return java.lang.String
     **/
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
    @RequestMapping(value = ("{usercode}"), method = RequestMethod.GET)
    public String home(@PathVariable String usercode,Model model,HttpServletRequest request, HttpServletResponse response) {
        FrontUser frontUser = new FrontUser();
        frontUser.setUserCode(usercode);
        frontUser = frontUserService.get(frontUser);
        if(frontUser!=null){
            model.addAttribute("frontUser",frontUser);
            return "modules/front/user/home";
        }else{
            try {
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 前台用户修改头像
     * @Date 17:23 2018/12/7
     * @Param [user, request]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("upload"), method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request,FrontUser frontUser) {
        frontUserService.upload(request,frontUser);
        return renderResult("true","上传成功");
    }
    /**
     * @Author xuyuxiang
     * @Description 前台用户修改基本信息
     * @Date 9:07 2018/12/12
     * @Param [user]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("infoSaveBase"), method = RequestMethod.POST)
    @ResponseBody
    public String infoSaveBase(FrontUser frontUser) {
        frontUserService.save(frontUser);
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
        FrontUser frontUser =  FrontUtils.getCurrentFrontUser();
        if(frontUser == null){
            return renderResult("false","请登录");
        }else if(!UserUtils.getSubject().isPermitted("front:view")){
            return renderResult("false","您的操作权限不足");
        }else if(FrontUtils.isSigned()){
            //判断是否签到过了
            return renderResult("false","今天已签到");
        }else{
            return renderResult("true","签到成功",frontUserService.sign(frontUser.getFront()));
        }
    }


}
