package org.muzima.turkana.web;

import org.springframework..annotation.Bean;
import org.springframework..annotation.Configuration;
import org.springframework..annotation.PropertySource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 6/24/19.
 */

@Configuration
@PropertySource("classpath:swagger.properties")
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.muzima.turkana.web.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Turkana Backup Server REST API",
                "API Sepcification for interaction with Muzima Messaging Backup Server",
                "0.0.1",
                "Terms of Service (Under Construction)",
                new Contact("Muzima Organization", "http://muzima.org", "info@muzima.org"),
                "Muzima Private License",
                "Request A Copy of License",
                Collections.emptyList()
        );
    }
}
