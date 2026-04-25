package shako.schoolmanagement.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Configuration
public class SecurityConstants {

    public static final int    EXPIRATION_TIME = 9000000;
    public static final String SECURITY_HEADER = "Authorization";
    public static final String TOKEN_PREFIX    = "Bearer ";
    public static final String JWT_AUTHORITIES = "authorities";

    private static final String RUNTIME_SECRET = generateRuntimeSecret();

    private static String generateRuntimeSecret() {
        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);
        String secret = Base64.getEncoder().encodeToString(key);
        log.info("JWT signing key generated for this session — all previous tokens are now invalid");
        return secret;
    }

    public static String getSecretToken() {
        return RUNTIME_SECRET;
    }
}
