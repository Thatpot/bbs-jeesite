package com.jeesite.modules.front.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @ClassName MultipartConfig
 * @Description 文件上传临时存储目录
 * @Author xuyux
 * @Date 2018/12/11 13:47
 **/
@Configuration
public class MultipartConfig {
    @Bean
    MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir")+"/data/tmp";
        File tmpFile = new File(location);
        if(!tmpFile.exists()){
            tmpFile.mkdirs();
        }
        multipartConfigFactory.setLocation(location);
        return  multipartConfigFactory.createMultipartConfig();
    }
}
