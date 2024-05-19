package com.apartment.house.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Apartment House",
            email = "mehmetcnyildiz@gmail.com"
        ),
        title = "Apartment House API",
        version = "1.0",
        description = "Apartment House API Documentation",
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0"
        ),
        termsOfService = "Terms of service"
    ),
    servers = {
        @Server(
            url = "http://localhost:8099",
            description = "Stage server"
        ),
        @Server(
            url = "http://localhost:8090",
            description = "Local server"
        ),
        @Server(
            url = "https://house-application-2c2abb3720c7.herokuapp.com",
            description = "Production server"
        )
    },
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Bearer Token",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
