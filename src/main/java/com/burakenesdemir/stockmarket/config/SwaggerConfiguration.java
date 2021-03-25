package com.burakenesdemir.stockmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.burakenesdemir.stockmarket.util.SwaggerConstants.*;
import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(withClassAnnotation(RestController.class))
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(PROJECT_NAME)
                .description(PROJECT_DESC).termsOfServiceUrl(EMPTY_STR)
                .contact(new Contact(PROJECT_CONTACT, PROJECT_URL, PROJECT_CONTACT_EMAIL))
                .license(PROJECT_LICENCE)
                .licenseUrl(PROJECT_LICENCE_URL)
                .version(PROJECT_VERSION)
                .build();
    }

}