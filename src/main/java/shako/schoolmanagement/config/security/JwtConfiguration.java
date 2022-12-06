package shako.schoolmanagement.config.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@NoArgsConstructor
public class JwtConfiguration {
    private String secretToken;
}
