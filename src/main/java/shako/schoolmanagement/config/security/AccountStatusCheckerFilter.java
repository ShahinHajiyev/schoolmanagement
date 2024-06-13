package shako.schoolmanagement.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.exception.StudentNotActiveRequestException;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.service.inter.UserService;
import shako.schoolmanagement.validator.LoginActivator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class AccountStatusCheckerFilter extends OncePerRequestFilter {



    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;


    public final MailService mailService;


    public final LoginActivator loginActivator;

    public final PasswordEncoder passwordEncoder;

    public final UserService userService;



    @Autowired
    public AccountStatusCheckerFilter(UserRepository userRepository,
                                      HandlerExceptionResolver handlerExceptionResolver,
                                      MailService mailService,
                                      LoginActivator loginActivator, PasswordEncoder passwordEncoder, UserService userService) {

        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.mailService = mailService;
        this.loginActivator = loginActivator;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        CachedBodyHttpServletRequest requestClone =
                new CachedBodyHttpServletRequest(request);

        String requestUrl = "http://localhost:8090/api/login";

        if (request.getRequestURL().toString().equals(requestUrl)) {
            Pair<String, String> credentials = extractUsername(requestClone);

            assert credentials != null;
            String username = credentials.getKey();
            String password = credentials.getValue();

            //User user = userRepository.findByNeptun(username);
            Optional<User> user = userRepository.findByNeptunCode(username);
            user.orElseThrow(() -> new UsernameNotFoundException("User does not exist"));


            boolean isPasswordMatches = passwordEncoder.matches(password, user.get().getPassword());


            if (!user.get().getIsActive() && isPasswordMatches) {
                handlerExceptionResolver.resolveException(requestClone, response, null, new StudentNotActiveRequestException("Student not active"));
                String token = loginActivator.activationTokenGenerator();
                userService.saveActivationCode(user.get(), token);

                mailService.sendMail(user.get().getEmail(),
                        "This is the activation code: ",
                        token);
                throw new StudentNotActiveRequestException("StudentNotActiveException");
            }
        }

            filterChain.doFilter(requestClone, response);
        }


    private Pair extractUsername(HttpServletRequestWrapper request) throws IOException {



       if (request.getMethod().equals(HttpMethod.POST.toString())) {
            try {


                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(request.getReader());

                String neptunCode = jsonNode.get("neptunCode").asText();
                String password = jsonNode.get("password").asText();

                //return jsonNode.get("neptunCode").asText();
                return Pair.of(neptunCode, password);


            } catch (Exception e) {

                throw new RuntimeException();
            }
        }

        return null;
    }
}


