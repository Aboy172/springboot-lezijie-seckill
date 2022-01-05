package com.example.java2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置
 *
 * @author cym 2022/1/5
 */
@Configuration
public class Swagger2Config {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example.java2.controller"))
        .paths(PathSelectors.any())
        .build();
  }
  // 构建 api文档的详细信息函数
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        // 页面标题
        .title("XX平台API接口文档")
        // 创建人
        .contact(new Contact("cym", "http://www.javachat.cc", "q15207553802@gmail.com"))
        // 版本号
        .version("1.0")
        // 描述
        .description("系统API描述")
        .build();
  }
}
