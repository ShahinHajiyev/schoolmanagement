package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shako.schoolmanagement.dto.UsernamePasswordDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    private  final AuthenticationManager authenticationManager;

    public AuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UsernamePasswordDto usernamePasswordDto = new ObjectMapper().
                    readValue(request.getInputStream(), UsernamePasswordDto.class);


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usernamePasswordDto.getUsername(),
                    usernamePasswordDto.getPassword()
            );

            return authenticationManager.authenticate(
                    /*new UsernamePasswordAuthenticationToken(
                          usernamePasswordDto.getUsername(),
                          usernamePasswordDto.getPassword()
            )*/
            authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        try{
            String token = Jwts.builder()
                    .setSubject(authResult.getName())
                    .claim(SecurityConstants.JWT_AUTHORITIES, authResult.getAuthorities())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(Keys.hmacShaKeyFor(SecurityConstants.getSecretToken().getBytes()))
                    .compact();
            response.addHeader(SecurityConstants.SECURITY_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }

    }
}
