package shako.schoolmanagement.config.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenVerifier extends OncePerRequestFilter {

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

        }catch (JwtException exception){
            throw new IllegalStateException("Token is not trusted ");
        }

        filterChain.doFilter(request,response);

    }
}
