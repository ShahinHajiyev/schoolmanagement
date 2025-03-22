package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    private  final AuthenticationManager authenticationManager;


    private final HandlerExceptionResolver exceptionResolver;

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);


    @Autowired
    public AuthFilter(AuthenticationManager authenticationManager, HandlerExceptionResolver exceptionResolver) {
        this.authenticationManager = authenticationManager;
        this.exceptionResolver = exceptionResolver;
    }


   @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {



       try {
           UsernamePasswordDto   usernamePasswordDto = new ObjectMapper().
                   readValue(request.getInputStream(), UsernamePasswordDto.class);
           return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   usernamePasswordDto.getNeptunCode(),
                   usernamePasswordDto.getPassword()));
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");

        if (failed.getMessage().equals("User is disabled")) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);

        }

        else response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Authentication failed");
        errorResponse.put("message", failed.getMessage());

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


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


