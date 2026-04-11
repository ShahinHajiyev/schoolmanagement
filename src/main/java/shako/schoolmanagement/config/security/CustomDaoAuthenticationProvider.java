package shako.schoolmanagement.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.service.inter.UserService;
import shako.schoolmanagement.validator.LoginActivator;

/**
 * Extends DaoAuthenticationProvider to add activation-email logic on first login
 * while a user's account is still disabled.
 *
 * Strategy: delegate the full authentication pipeline to super.authenticate()
 * (password check, pre/post checks, account-expired, locked, credentials-expired).
 * Intercept only DisabledException to inject the email side-effect, then rethrow.
 * This avoids re-implementing Spring Security's internal verification chain.
 */
@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final LoginActivator loginActivator;
    private final MailService mailService;
    private final UserService userService;
    private final UserRepository userRepository;

    public CustomDaoAuthenticationProvider(LoginActivator loginActivator, MailService mailService,
                                           UserService userService, UserRepository userRepository) {
        this.loginActivator = loginActivator;
        this.mailService = mailService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            return super.authenticate(authentication);

        } catch (DisabledException ex) {
            String username = authentication.getName();
            log.info("Account not activated for user: {} — sending activation code", username);
            try {
                User userFromDb = userRepository.findByNeptun(username);
                String token = loginActivator.activationTokenGenerator();
                userService.saveActivationCode(userFromDb, token);
                mailService.sendMail(userFromDb.getEmail(), "Activation code", token);
            } catch (Exception mailEx) {
                log.error("Failed to send activation email to user {}: {}", username, mailEx.getMessage(), mailEx);
            }
            throw ex;   // rethrow so AuthFilter.unsuccessfulAuthentication() returns 409
        }
    }
}
