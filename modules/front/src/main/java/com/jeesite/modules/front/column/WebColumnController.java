package com.jeesite.modules.front.column;

import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontPost;
import com.jeesite.modules.front.service.FrontPostService;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.sys.utils.DictUtils;
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
@RequestMapping(value = "${frontPath}/front/column")
public class WebColumnController extends BaseController {
    @Autowired
    private FrontPostService postService;
    /**
     * @Author xuyuxiang
     * @Description 各分类的主页
     * @Date 16:55 2018/12/25
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("{category}"), method = RequestMethod.GET)
    public String templateIndex(Model model, @PathVariable String category, HttpServletRequest request, HttpServletResponse response) {
        FrontPost frontPost = new FrontPost();
        String dicCategoryLabel = DictUtils.getDictLabel("front_post_category",category,null);
        if(dicCategoryLabel!=null){
            if(!"6".equals(dicCategoryLabel)){
                frontPost.setPostCategory(dicCategoryLabel);
            }
            frontPost.setPage(new Page<>(request, response));
            model.addAttribute("postPage",postService.findPage(frontPost));
            model.addAttribute("category",category);
            model.addAttribute("poststatus",null);
            return "modules/front/column/template";
        }
        try {
            response.sendError(404);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @Author xuyuxiang
     * @Description 各分类下的状态页
     * @Date 16:55 2018/12/25
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("{category}/{poststatus}"), method = RequestMethod.GET)
    public String templateSub(Model model,@PathVariable String category,@PathVariable String poststatus, HttpServletRequest request, HttpServletResponse response) {
        FrontPost frontPost = new FrontPost();
        String dicCategoryLabel = DictUtils.getDictLabel("front_post_category",category,null);
        String dicPostStatusValue = DictUtils.getDictValue("front_post_category_status",poststatus,null);
        if(dicCategoryLabel!=null && dicPostStatusValue!=null){
            if(!"6".equals(dicCategoryLabel)){
                frontPost.setPostCategory(dicCategoryLabel);
            }
            //精华，设置查询精贴为1
            if("2".equals(dicPostStatusValue)){
                frontPost.setPostIsgood("1");
            }else{
                frontPost.setPostStatus(dicPostStatusValue);
            }
            frontPost.setPage(new Page<>(request, response));
            model.addAttribute("postPage",postService.findPage(frontPost));
            model.addAttribute("category",category);
            model.addAttribute("poststatus",poststatus);
            return "modules/front/column/template";
        }
        try {
            response.sendError(404);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
