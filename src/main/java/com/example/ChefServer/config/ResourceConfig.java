package com.example.ChefServer.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String upload_path;

    @Value("${photo.path}")
    private String photo_path;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file://" + upload_path + "/");
        registry.addResourceHandler("/photos/**").addResourceLocations("file://" + photo_path + "/");
    }
}
