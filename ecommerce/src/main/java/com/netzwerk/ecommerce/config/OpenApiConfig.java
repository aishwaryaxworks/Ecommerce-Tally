package com.netzwerk.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Ecommerce API",
                version = "1.0",
                description = "API for interacting with interacting with Tally using a Web Application",
                contact = @Contact(name = "Your Name", email = "your.email@example.com")
        )
)
public class OpenApiConfig {
}
