package com.jeesite.modules.front.jie;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.front.entity.*;
import com.jeesite.modules.front.service.*;
import com.jeesite.modules.front.utils.FrontUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontCollectService frontCollectService;
    @Autowired
    private FrontCommentService frontCommentService;
    @Autowired
    private FrontAdService frontAdService;

    /**
     * @Author xuyuxiang
     * @Description 获取帖子
     * @Date 10:17 2018/12/28
     * @Param [id, isNewRecord]
     * @return com.jeesite.modules.front.entity.FrontPost
     **/
   @ModelAttribute
    public FrontPost getFrontPost(String id, boolean isNewRecord,String postUpCode){
        FrontPost frontPost = frontPostService.get(id, isNewRecord);
        if(frontPost.getPostUp()==null && StringUtils.isNotBlank(postUpCode)){
            frontPost.setPostUp(frontUserService.get(postUpCode));
        }
        return frontPost;
    }

    /**
     * @Author xuyuxiang
     * @Description 发表新帖跳转
     * @Date 11:56 2018/12/24
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("add"), method = RequestMethod.GET)
    public String add(FrontPost frontPost,HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("frontPost",frontPost);
        model.addAttribute("frontUser",FrontUtils.getCurrentFrontUser());
        return "modules/front/jie/add";
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
        //postUpCode.upCode
        if(post.getIsNewRecord()) {
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
            //是否允许回复
            post.setPostIsreply("0");
            //如果是分享，设置状态待审核
            if("1".equals(post.getPostCategory())){
                post.setStatus("2");
            }
        }
        frontPostService.save(post);
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
    public String postDetail(Model model, @PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        FrontPost frontPost = frontPostService.get(id);
        if(frontPost != null){
            frontPost.setPostViewCount(frontPost.getPostViewCount()+1);
            frontPostService.save(frontPost);
            model.addAttribute("frontPost",frontPost);
            FrontComment frontComment = new FrontComment(frontPost);
            frontComment.setPage(new Page<>(request, response));
            model.addAttribute("commentPage",frontCommentService.findPage(frontComment));
            model.addAttribute("hasAccept",frontPostService.hasAccept(frontPost));
            model.addAttribute("frontUser",FrontUtils.getCurrentFrontUser());
            FrontPost example = new FrontPost();
            example.setCreateDate_between(FrontUtils.getTimeInterval());
            model.addAttribute("hotPostList",frontPostService.findHotPlostList(example,10));
            FrontAd frontAd = new FrontAd();
            //官方产品
            frontAd.setPage(new Page<FrontAd>());
            frontAd.setAdType("0");
            model.addAttribute("officalAdList",frontAdService.findPage(frontAd).getList());
            //皇冠赞助商
            frontAd.setAdType("1");
            model.addAttribute("heartAdList",frontAdService.findPage(frontAd).getList());
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
    /**
     * @Author xuyuxiang
     * @Description 帖子评论
     * @Date 10:08 2018/12/27
     * @Param [model, frontPost]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("reply"), method = RequestMethod.POST)
    @ResponseBody
    public String reply(Model model, FrontPost frontPost, FrontComment frontComment) {
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        if(frontUser == null){
            return renderResult(Global.FALSE,"请登录");
        }else if(!UserUtils.getSubject().isPermitted("front:edit")){
            return renderResult(Global.FALSE,"您的操作权限不足");
        }
        //设置id为空，防止post的id带来的影响
        frontComment.setId("");
        frontComment.setCommentIsaccept("0");
        frontComment.setCommentUp(frontUser);
        frontComment.setIsNewRecord(true);
        List<FrontComment> list = ListUtils.newArrayList();
        list.add(frontComment);
        frontPost.setPostCommentCount(frontPost.getPostCommentCount()+1);
        frontPost.setFrontCommentList(list);
        frontPostService.save(frontPost);
        Long count = frontUser.getFront().getUpCommentCount() + new Long(1);
        frontUser.getFront().setUpCommentCount(count);
        frontUserService.save(frontUser);
        return renderResult(Global.TRUE,"评论成功");
    }

    /**
     * @Author xuyuxiang
     * @Description 删除帖子
     * @Date 14:25 2018/12/28
     * @Param [frontPost]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("delete"), method = RequestMethod.POST)
    @ResponseBody
    public String delete(FrontPost frontPost) {
        frontPostService.delete(frontPost);
        return renderResult(Global.TRUE, text("删除成功！"));
    }

    /**
     * @Author xuyuxiang
     * @Description 审核通过帖子
     * @Date 14:25 2018/12/28
     * @Param [frontPost]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("passOrBack"), method = RequestMethod.POST)
    @ResponseBody
    public String passOrBack(FrontPost frontPost,String status) {
        frontPost.setStatus(status);
        frontPostService.updateStatus(frontPost);
        return renderResult(Global.TRUE, text("审核通过！"));
    }

    /**
     * @Author xuyuxiang
     * @Description 置顶或加精
     * @Date 14:45 2018/12/28
     * @Param [frontPost]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("topOrGoodPost"), method = RequestMethod.POST)
    @ResponseBody
    public String topOrGoodPost(FrontPost frontPost,String postIsgood,String postIstop) {
        if(StringUtils.isNotEmpty(postIsgood)) {
            frontPost.setPostIsgood(postIsgood);
        }
        if(StringUtils.isNotEmpty(postIstop)){
            frontPost.setPostIstop(postIstop);
        }
        frontPostService.save(frontPost);
        return renderResult(Global.TRUE, text("操作成功！"));
    }
    /**
     * @Author xuyuxiang
     * @Description 收藏帖子
     * @Date 17:01 2018/12/28
     * @Param [frontPost]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("collect"), method = RequestMethod.POST)
    @ResponseBody
    public String collect(FrontCollect frontCollect,String type) {
        if("add".equals(type)){
            frontCollect.setIsNewRecord(true);
            frontCollect.setUserCode(FrontUtils.getCurrentFrontUser().getUserCode());
            frontCollectService.save(frontCollect);
        }
        if("remove".equals(type)){
            frontCollect.setUserCode(FrontUtils.getCurrentFrontUser().getUserCode());
            frontCollect = frontCollectService.getByEntity(frontCollect);
            frontCollectService.delete(frontCollect);
        }
        return renderResult(Global.TRUE, text("操作成功！"));
    }
    /**
     * @Author xuyuxiang
     * @Description 异步加载该帖子是否被收藏
     * @Date 15:59 2018/12/29
     * @Param [frontPost]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("isCollected"), method = RequestMethod.POST)
    @ResponseBody
    public String isCollected(String collectPostId) {
        FrontUser frontUser = FrontUtils.getCurrentFrontUser();
        if(frontUser!=null){
            FrontCollect frontCollect = new FrontCollect();
            FrontPost frontPost = new FrontPost();
            frontPost.setId(collectPostId);
            frontCollect.setCollectPost(frontPost);
            frontCollect.setUserCode(frontUser.getUserCode());
            frontCollect = frontCollectService.getByEntity(frontCollect);
            if(frontCollect != null){
                return renderResult(Global.TRUE, text("该帖子已被收藏"),true);
            }else{
                return renderResult(Global.TRUE, text("该帖子未被收藏"),false);
            }
        }else{
            return renderResult(Global.TRUE, text("未登录"));
        }

    }
    /**
     * @Author xuyuxiang
     * @Description 删除回帖
     * @Date 11:37 2019/1/2
     * @Param [frontComment]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("deleteReply"), method = RequestMethod.POST)
    @ResponseBody
    public String deleteReply(FrontComment frontComment) {
        frontComment = frontCommentService.get(frontComment);
        frontCommentService.delete(frontComment);
        //如果删除的是最佳答案，则
        if("1".equals(frontComment.getCommentIsaccept())){
            FrontPost frontPost = frontPostService.get(frontComment.getPostId());
            frontPost.setPostStatus("0");
            frontPostService.save(frontPost);
        }
        return renderResult(Global.TRUE, text("删除评论成功！"));
    }
    /**
     * @Author xuyuxiang
     * @Description 采纳为最佳答案
     * @Date 11:44 2019/1/2
     * @Param [frontComment]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("acceptReply"), method = RequestMethod.POST)
    @ResponseBody
    public String acceptReply(FrontComment frontComment) {
        frontComment = frontCommentService.get(frontComment);
        frontComment.setCommentIsaccept("1");
        frontCommentService.save(frontComment);
        FrontPost frontPost = frontPostService.get(frontComment.getPostId());
        frontPost.setPostStatus("1");
        frontPostService.save(frontPost);
        return renderResult(Global.TRUE, text("采纳成功！"));
    }

}
