package com.example.java2.config;

import com.example.java2.LocaleResolver.MyLocaleResolver;
import com.example.java2.controller.AccessLimitInterceptor;
import com.example.java2.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMVC配置
 *
 * @author cym 2021/12/21
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired private UserArgumentResolver userArgumentResolver;
  @Autowired private LoginInterceptor loginInterceptor;
  @Autowired private AccessLimitInterceptor accessLimitInterceptor;

  // 自定义参数
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(userArgumentResolver);
  }

  /** 默认配置类大于配置文件， 所以如果写了配置类以后是不会再去拿配置文件的的东西 */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(loginInterceptor)
        .addPathPatterns("/goods/**")
        .excludePathPatterns("/login");
    registry.addInterceptor(accessLimitInterceptor);
    WebMvcConfigurer.super.addInterceptors(registry);
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new MyLocaleResolver();
  }
}
