package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import shako.schoolmanagement.exception.ErrorResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validates the JWT on every request.
 *
 * Error handling note: filters live outside the Spring MVC dispatcher,
 * so @ControllerAdvice cannot catch exceptions thrown here.
 * Both JWT error paths write the JSON response directly and return —
 * filterChain.doFilter() is NOT called afterwards, preventing double-writes.
 */
@Slf4j
public class TokenVerifier extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(SecurityConstants.SECURITY_HEADER);

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");

            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SecurityConstants.getSecretToken().getBytes()))
                    .build()
                    .parseClaimsJws(token);

            Claims body = jws.getBody();
            String userName = body.getSubject();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> authorities =
                    (List<Map<String, String>>) body.get(SecurityConstants.JWT_AUTHORITIES);

            List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userName, null, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Token verified for user: {}", userName);

        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT on [{}]", request.getRequestURI());
            writeError(response, HttpStatus.UNAUTHORIZED, "Token expired");
            return;

        } catch (JwtException ex) {
            log.warn("Invalid JWT on [{}]: {}", request.getRequestURI(), ex.getMessage());
            writeError(response, HttpStatus.UNAUTHORIZED, "Token is invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(),
                new ErrorResponse(status.value(), status.getReasonPhrase(), message));
    }
}
