package shako.schoolmanagement.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.service.inter.UserService;
import shako.schoolmanagement.validator.LoginActivator;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final LoginActivator loginActivator;
    private final MailService mailService;

    private final UserService userService;
    private final UserRepository userRepository;

    public CustomDaoAuthenticationProvider(LoginActivator loginActivator, MailService mailService, UserService userService, UserRepository userRepository){

        this.loginActivator = loginActivator;
        this.mailService = mailService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws    AuthenticationException {
        String username = authentication.getName();
        String presentedPassword = authentication.getCredentials().toString();

        UserDetails user = getUserDetailsService().loadUserByUsername(username);

        if (!getPasswordEncoder().matches(presentedPassword, user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        if (!user.isEnabled()) {

            User userFromDb = userRepository.findByNeptun(username);
                    String token = loginActivator.activationTokenGenerator();
                    userService.saveActivationCode(userFromDb, token);

                    mailService.sendMail(userFromDb.getEmail(),
                            "This is the activation code: ",
                            token);

            throw new DisabledException("User is disabled");
        }

        // (Optional) You can perform other checks here, such as checking account locked/expired, etc.

        // 3. If everything is fine, create a successful authentication token.
        // You might want to call additional methods (like creating authorities) as needed.
        return createSuccessAuthentication(user, authentication,user);
}
}




