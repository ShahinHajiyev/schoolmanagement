package shako.schoolmanagement.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${allowed.origins}")
    private String[] theAllowedOrigins;

    @Value("${allowed.headers}")
    private String[] theAllowedHeaders;

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    @Override
    public void addCorsMappings(CorsRegistry cors) {

        cors.addMapping(basePath + "/**")
                .allowedOrigins(theAllowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Access-Control-Expose-Headers", "Authorization", "Origin", "X-Requested-With", "Content-Type", "Accept", "Access-Control-Allow-Origin")
                .exposedHeaders("Authorization");

    }
}
