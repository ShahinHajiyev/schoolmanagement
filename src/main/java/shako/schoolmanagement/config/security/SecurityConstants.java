package shako.schoolmanagement.config.security;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConstants {
    public static final int REMEMBER_ME_EXPIRATION_TIME = 86400000; // 1 day
}
