package shako.schoolmanagement.config.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import shako.schoolmanagement.exception.RestError;

import java.util.stream.Collectors;

public class TokenVerifier extends OncePerRequestFilter {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(TokenVerifier.class);


    @Autowired
    RestError restError;


    private final HandlerExceptionResolver handlerExceptionResolver;


    Exception generalEX = new Exception();

    @Autowired
    public TokenVerifier(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(SecurityConstants.SECURITY_HEADER);

        if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)){
            filterChain.doFilter(request,response);
            return;
        }

        Jws<Claims> jws;
        String token = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");

        try{

            jws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SecurityConstants.getSecretToken().getBytes()))
                    .build()
                    .parseClaimsJws(token);

            Claims body = jws.getBody();
            String userName = body.getSubject();
            List<Map<String,String>> authorities = (List<Map<String, String>>)  body.get(SecurityConstants.JWT_AUTHORITIES);

            List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream().map(m->new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toList());



            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userName,
                    null,
                    grantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException exception) {

            //handlerExceptionResolver.resolveException(request, response, null, exception);
            throw new ExpiredJwtException(null, null, "Token expired");

        } catch (JwtException exception){

            generalEX=exception;
            handlerExceptionResolver.resolveException(request, response, null, exception);
            throw new JwtException("Token is not trusted ");
        }
        

        filterChain.doFilter(request,response);

    }
}
