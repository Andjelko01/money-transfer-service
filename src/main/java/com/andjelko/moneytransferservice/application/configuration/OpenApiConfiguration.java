package com.andjelko.moneytransferservice.application.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for Swagger documentation.
 * This configuration sets up the API documentation with basic information
 * and makes it available at /swagger-ui/index.html
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Money Transfer Service API")
                        .version("1.0.0")
                        .description("REST API for money transfer operations between accounts")
                        .contact(new Contact()
                                .name("Nemanja Andjelkovicm")
                                .email("nemanja11andjelkovicm@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
