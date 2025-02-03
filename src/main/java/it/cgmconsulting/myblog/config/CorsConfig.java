package it.cgmconsulting.myblog.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
public class CorsConfig {
    @Bean public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); // ("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
