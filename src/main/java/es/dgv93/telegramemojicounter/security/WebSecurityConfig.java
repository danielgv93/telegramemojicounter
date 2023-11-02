package es.dgv93.telegramemojicounter.security;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    @Value("*")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        log.info("Configuring CORS....");
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            registry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedOrigins(allowedOrigins)
                    .allowedHeaders("*");
        }
    }
}