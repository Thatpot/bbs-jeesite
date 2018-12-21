package com.jeesite.modules.front.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.front.entity.Front;
import com.jeesite.modules.front.entity.FrontUser;
import com.jeesite.modules.front.service.FrontService;
import com.jeesite.modules.front.service.FrontUserService;
import com.jeesite.modules.front.utils.FrontUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private FrontUserService frontUserService;
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
    @RequestMapping(value = ("signin"), method = RequestMethod.GET)
    @ResponseBody
    public String signin(){
        return renderResult("true","签到成功",frontUserService.signin());
    }
}
