package com.jeesite.modules.front.web;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.utils.FrontUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName FrontController
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/5 9:46
 **/
@Controller
@RequestMapping(value = "${frontPath}/front")
public class FrontController extends BaseController {
    @Autowired
    private FrontService frontService;
    /**
     * @Author xuyuxiang
     * @Description 首页
     * @Date 9:53 2018/12/7
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = ("index"), method = RequestMethod.GET)
    public String index(Model model) {
        Front front = frontService.getCurrentFront();
        if(front!=null){
            model.addAttribute("map",frontService.getCurrentFrontSignCountAndKiss(front));
        }
        return "modules/front/index";
    }

    /**
     * @Author xuyuxiang
     * @Description 签到活跃榜
     * @Date 15:50 2018/12/20
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = ("signin"))
    @ResponseBody
    public String signin(){
        return null;
    }
}
