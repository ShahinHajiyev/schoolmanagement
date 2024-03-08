package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;
import shako.schoolmanagement.config.auth.AppUserDetails;
import shako.schoolmanagement.config.auth.AppUserDetailsService;
import shako.schoolmanagement.dto.UsernamePasswordDto;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.exception.StudentNotActiveRequestException;
import shako.schoolmanagement.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.Optional;


public class AccountStatusCheckerFilter extends OncePerRequestFilter {



    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public AccountStatusCheckerFilter(UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {

        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {




        //HttpServletRequestWrapper requestClone = new ContentCachingRequestWrapper(request);

        //ClonedHttpServletRequest requestClone = new ClonedHttpServletRequest(request);

        CachedBodyHttpServletRequest requestClone =
                new CachedBodyHttpServletRequest(request);


       String username = extractUsername(requestClone);
        Optional <User> user = userRepository.findByNeptunCode(username);



        if (user.isPresent() && !user.get().getIsActive()) {
            handlerExceptionResolver.resolveException(requestClone, response, null, new StudentNotActiveRequestException("Student not active"));
            throw new StudentNotActiveRequestException("StudentNotActiveException");

        }



            filterChain.doFilter(requestClone, response);
        }


    private String extractUsername(HttpServletRequestWrapper request) throws IOException {





       if (request.getMethod().equals(HttpMethod.POST.toString())) {
            try {

            /*    UsernamePasswordDto objectMapper = new ObjectMapper().readValue(request.getInputStream(), UsernamePasswordDto.class);


                return objectMapper.getNeptunCode();*/


                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(request.getReader());

                return jsonNode.get("neptunCode").asText();

 /*               StringBuilder jsonBody = new StringBuilder();
                try (BufferedReader reader = request.getReader()) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBody.append(line);
                    }
                }

                JsonObject jsonObject = Json.createReader(new StringReader(jsonBody.toString())).readObject();
                String neptunCode = jsonObject.getString("neptunCode");
                return neptunCode;*/

            } catch (Exception e) {

                throw new RuntimeException();
            }
        }

        return null;
    }
}



  class CachedBodyHttpServletRequest  extends HttpServletRequestWrapper {
      private byte[] cachedBody;

      public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
          super(request);
          InputStream requestInputStream = request.getInputStream();
          this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
      }

      @Override
      public ServletInputStream getInputStream() throws IOException {
          return new CachedBodyServletInputStream(this.cachedBody);
      }

      @Override
      public BufferedReader getReader() throws IOException {
          ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
          return new BufferedReader(new InputStreamReader(byteArrayInputStream));
      }

    }


class CachedBodyServletInputStream extends ServletInputStream {

    private InputStream cachedBodyInputStream;

    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public boolean isFinished() {
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }
}
