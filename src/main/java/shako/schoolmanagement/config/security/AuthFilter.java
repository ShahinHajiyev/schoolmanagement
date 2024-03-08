package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import shako.schoolmanagement.dto.UsernamePasswordDto;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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

       System.out.println("Here");

        try {
            UsernamePasswordDto usernamePasswordDto = new ObjectMapper().
                    readValue(request.getInputStream(), UsernamePasswordDto.class);


            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    usernamePasswordDto.getNeptunCode(),
                    usernamePasswordDto.getPassword()));

        }  catch (BadCredentialsException  | IOException ex ) {
            exceptionResolver.resolveException(request,response,null,new shako.schoolmanagement.exception.BadCredentialsException("Bad credentials"));
        throw new shako.schoolmanagement.exception.BadCredentialsException("Bad Credentials");
    }
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

        public String passwordEncode(String password) {

        return  new BCryptPasswordEncoder().encode(password);
       }

    }




/*    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        //super.unsuccessfulAuthentication(request, response, failed);

        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        response.sendError(statusCode, "AAA");
    }*/

