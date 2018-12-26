package com.jeesite.modules.front.jie;

import com.jeesite.common.entity.Page;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.file.entity.FileUploadParms;
import com.jeesite.modules.front.dao.FrontCommentDao;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontComment;
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontPostService;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.front.utils.FrontUtils;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.DictUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName FrontController
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/5 9:46
 **/
@Controller
@RequestMapping(value = "${frontPath}/front/jie")
public class WebJieController extends BaseController {
    @Autowired
    private FrontPostService frontPostService;
    /**
     * @Author xuyuxiang
     * @Description 去发表新帖，Ajax调用
     * @Date 11:56 2018/12/24
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("toAdd"), method = RequestMethod.POST)
    @ResponseBody
    public String toAdd(Model model) {
        LoginInfo loginInfo = UserUtils.getLoginInfo();
        //如果没登录跳转到登录页
        if(loginInfo == null){
            return renderResult("false","请登录");
        }else{//如果登录了跳转到首页
            return renderResult("true","已登录");
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 发表新帖跳转
     * @Date 11:56 2018/12/24
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("add"), method = RequestMethod.GET)
    public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
        LoginInfo loginInfo = UserUtils.getLoginInfo();
        //如果没登录跳转到登录页
        if(loginInfo == null){
            ServletUtils.redirectUrl(request, response,frontPath+"/front/user/login");
            return null;
        }else{//如果登录了跳转到首页
            return "modules/front/jie/add";
        }
    }
    /**
     * @Author xuyuxiang
     * @Description 发表新帖子时上传图片
     * @Date 16:41 2018/12/24
     * @Param [request]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("upload"), method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request) {
        FileUploadParms params = frontPostService.upload(request);
        return renderResult("true","初始化文件参数成功",params);
    }

    /**
     * @Author xuyuxiang
     * @Description 发表新帖
     * @Date 14:26 2018/12/24
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("post"), method = RequestMethod.POST)
    @ResponseBody
    public String post(Model model, FrontPost post) {
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        if(post.getIsNewRecord()){
            //作者
            post.setPostAuth(frontUser.getUserName());
            //头像
            post.setPostAuthAvatar(frontUser.getAvatarUrl());
            //VIP等级
            post.setPostAuthVlevel(frontUser.getFront().getUpVlevel());
            //认证信息
            post.setPostAuthInfo(frontUser.getFront().getUpAuth());
            //结束状态
            post.setPostStatus("0");
            //评论人数
            post.setPostCommentCount(new Long(0));
            //查看人数
            post.setPostViewCount(new Long(0));
            //是否置顶
            post.setPostIstop("0");
            //是否精贴
            post.setPostIsgood("0");
            frontPostService.save(post);
        }else{
            frontPostService.save(post);
        }
        return renderResult("true","发表成功");
    }
    /**
     * @Author xuyuxiang
     * @Description 查看帖子详情
     * @Date 16:50 2018/12/26
     * @Param [model, id, request, response]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("{id}"), method = RequestMethod.GET)
    public String templateIndex(Model model, @PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        FrontPost frontPost = frontPostService.get(id);
        if(frontPost != null){
            frontPost.setPostViewCount(frontPost.getPostViewCount()+1);
            frontPostService.save(frontPost);
            model.addAttribute("frontPost",frontPost);
            return "modules/front/jie/detail";
        }else {
            try {
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @RequestMapping(value = ("reply"), method = RequestMethod.POST)
    @ResponseBody
    public String reply(Model model, FrontComment frontComment) {
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        if (frontUser==null){
            return renderResult("false","请登录");
        }
        
        return renderResult("true","评论成功");
    }
}
