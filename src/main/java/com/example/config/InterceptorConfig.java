package com.example.config;

import com.example.switcher.DataSourceHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new DataSourceHandlerInterceptor()).addPathPatterns("/**");
    WebMvcConfigurer.super.addInterceptors(registry);
  }
}
