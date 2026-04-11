package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import shako.schoolmanagement.dto.UsernamePasswordDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthFilter(AuthenticationManager authenticationManager,
                      HandlerExceptionResolver unusedResolver) {
        this.authenticationManager = authenticationManager;
        // unusedResolver kept in signature so WebSecurityConfiguration compiles unchanged
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UsernamePasswordDto dto = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernamePasswordDto.class);
            log.info("Authentication attempt for user: {}", dto.getNeptunCode());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getNeptunCode(), dto.getPassword()));
        } catch (IOException e) {
            log.error("Failed to parse authentication request body", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.warn("Authentication failed for request [{}]: {}", request.getRequestURI(), failed.getMessage());

        response.setContentType("application/json;charset=UTF-8");

        if ("User is disabled".equals(failed.getMessage())) {
            log.warn("Login blocked — account not activated");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        Map<String, String> body = new HashMap<>();
        body.put("error", "Authentication failed");
        body.put("message", failed.getMessage());
        new ObjectMapper().writeValue(response.getWriter(), body);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("Authentication successful for user: {}", authResult.getName());

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim(SecurityConstants.JWT_AUTHORITIES, authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.getSecretToken().getBytes()))
                .compact();
        response.addHeader(SecurityConstants.SECURITY_HEADER, SecurityConstants.TOKEN_PREFIX + token);
    }
}
