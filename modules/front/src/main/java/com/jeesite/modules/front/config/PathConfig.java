package com.jeesite.modules.front.config;

import com.jeesite.common.beetl.BeetlUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.io.PropertiesUtils;
import org.beetl.core.GroupTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @ClassName PathConfig
 * @Description 前台页面路径配置，设置Beetl共享变量
 * @Author xuyux
 * @Date 2018/12/13 13:47
 **/
@Configuration
public class PathConfig implements InitializingBean {

    private GroupTemplate groupTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 设置Beetl GroupTemplate，如果不设置，取上下文中唯一的GroupTemplate对象
        this.groupTemplate = BeetlUtils.getResourceGroupTemplate();
        // 设置Beetl全局变量
        Map<String, Object> sharedVars = this.groupTemplate.getSharedVars();
        if (sharedVars == null){
            sharedVars = MapUtils.newHashMap();
        }
        //通过读取配置文件获取项目根路径：/js
        String contentPath =  PropertiesUtils.getInstance().getProperty("server.servlet.context-path");
        //通过读取配置文件获取项目前端路径 ：/f
        String frontPath = PropertiesUtils.getInstance().getProperty("frontPath");
        String adminPath = PropertiesUtils.getInstance().getProperty("adminPath");

        String ctxf = contentPath + frontPath;
        String ctxa = contentPath + adminPath;

        //最终路径/js
        sharedVars.put("ctxp", contentPath);
        //最终路径/js/f
        sharedVars.put("ctxf", ctxf);
        //最终路径/js/a
        sharedVars.put("ctxa", ctxa);

        this.groupTemplate.setSharedVars(sharedVars);
    }
}
