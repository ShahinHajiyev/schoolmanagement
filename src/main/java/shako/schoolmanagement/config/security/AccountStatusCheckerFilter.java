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
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.validator.LoginValidator;

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


    public final MailService mailService;


    public final LoginValidator loginValidator;

    @Autowired
    public AccountStatusCheckerFilter(UserRepository userRepository,
                                      HandlerExceptionResolver handlerExceptionResolver,
                                      MailService mailService,
                                      LoginValidator loginValidator) {

        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.mailService = mailService;
        this.loginValidator = loginValidator;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        CachedBodyHttpServletRequest requestClone =
                new CachedBodyHttpServletRequest(request);


       String username = extractUsername(requestClone);
        Optional <User> user = userRepository.findByNeptunCode(username);



        if (user.isPresent() && !user.get().getIsActive()) {
            String token = loginValidator.validationTokenGenerator();
            mailService.sendMail(user.get().getEmail(),
                    "This is the activation code: ",
                    token);
            handlerExceptionResolver.resolveException(requestClone, response, null, new StudentNotActiveRequestException("Student not active"));
            throw new StudentNotActiveRequestException("StudentNotActiveException");

        }

            filterChain.doFilter(requestClone, response);
        }


    private String extractUsername(HttpServletRequestWrapper request) throws IOException {


       if (request.getMethod().equals(HttpMethod.POST.toString())) {
            try {


                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(request.getReader());

                return jsonNode.get("neptunCode").asText();


            } catch (Exception e) {

                throw new RuntimeException();
            }
        }

        return null;
    }
}


