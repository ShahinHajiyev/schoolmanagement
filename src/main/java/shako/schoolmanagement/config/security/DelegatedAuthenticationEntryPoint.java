package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import shako.schoolmanagement.exception.StatusCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(
"delegatedAuthenticationEntryPoint"
)
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Qualifier("handlerExceptionResolver")
    @Autowired
    public HandlerExceptionResolver handlerExceptionResolver;

    ObjectMapper mapper = new ObjectMapper();


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {


                    System.out.println(authException);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\",  " +
                "                              \"statusCode\": \"" + StatusCode.UNAUTHORIZED + "\" }");


       handlerExceptionResolver.resolveException(request, response, null, authException);
    }
}
