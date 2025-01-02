package com.hd.config;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "HD API",
                description = "api명세서입니다.",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

//    @Bean
//    public OpenAPI openAPI(){
//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER).name("Authorization");
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
//
//        return new OpenAPI()
//                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
//                .security(Arrays.asList(securityRequirement));
//    }

//    @Bean
//    public GroupedOpenApi ApiLogin() {
//        String[] paths = {"/api/login/**"};
//
//        return GroupedOpenApi.builder()
//                .group("API Login")
//                .pathsToMatch(paths)
//                .build();
//    }
    @Bean
    public GroupedOpenApi ApiItem() {
        String[] paths = {"/api/item/**"};

        return GroupedOpenApi.builder()
                .group("API Item V0.1")
                .pathsToMatch(paths)
                .build();
    }
}