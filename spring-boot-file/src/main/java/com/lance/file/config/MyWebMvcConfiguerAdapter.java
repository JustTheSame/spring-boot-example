package com.lance.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhaotian
 */
@Configuration
public class MyWebMvcConfiguerAdapter implements WebMvcConfigurer {

    @Value("${file.rootPath}")
    private String ROOT_PATH;

    @Value("${file.sonPath}")
    private String SON_PATH;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = "file:" + ROOT_PATH + SON_PATH;
        registry.addResourceHandler("/img/**").addResourceLocations(filePath);
    }
}
