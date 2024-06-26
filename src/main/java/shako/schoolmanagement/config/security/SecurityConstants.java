package shako.schoolmanagement.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConstants {

    public static JwtConfiguration jwtConfiguration;

    @Autowired
    public SecurityConstants(JwtConfiguration jwtConfiguration) {
        SecurityConstants.jwtConfiguration = jwtConfiguration;
    }

    public static final int EXPIRATION_TIME = 9000000;
    public static final String SECURITY_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String JWT_AUTHORITIES = "authorities";

    public static String getSecretToken(){
        return jwtConfiguration.getSecretToken();
    }

}
