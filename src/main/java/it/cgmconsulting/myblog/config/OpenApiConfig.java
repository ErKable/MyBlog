package it.cgmconsulting.myblog.config;

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
                        name = "CGM Consulting",
                        email = "info-myblog@cgmconsulting.it",
                        url = "https://cgmconsulting.it/corsi-di-formazione-gratuiti/"
                ),
                description = "OpenApi documentation for MyBlog.",
                title = "OpenApi specification - CGM Consulting",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://my-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                //Ambienti di sviluppo e produzione
                @Server(
                        description = "DEV Enviroment",
                        url = "http://localhost:${server.port}"
                ),
                @Server(
                        description = "PROD Enviroment",
                        url = "https://myblog.cgmconsulting.it"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
//aggiunto solo perché usiamo SpringSecuirty, altrimenti non servirebbe
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT for CGM MyBlog Application",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
