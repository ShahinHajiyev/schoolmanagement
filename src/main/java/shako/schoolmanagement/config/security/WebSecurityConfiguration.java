package shako.schoolmanagement.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;
import shako.schoolmanagement.config.auth.AppUserDetailsService;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.service.inter.UserService;
import shako.schoolmanagement.validator.LoginActivator;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private LoginActivator loginActivator;

    @Autowired
    private UserService userService;

    @Autowired
    private DelegatedAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors().and()
                .authorizeRequests(customizer -> customizer
                        .antMatchers(HttpMethod.GET, "index", "/css", "/js", "/error").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .antMatchers("/api/user/register").permitAll()
                        .antMatchers("/api/user/activate").permitAll()
                        .antMatchers("/api/user/resend-activation").permitAll()
                        .antMatchers("/api/user/forgot-password").permitAll()
                        .antMatchers("/api/user/reset-password").permitAll()
                        .antMatchers("/api/auth/refresh").permitAll()
                        .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .antMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Handle missing/absent token (no auth at all) with a JSON 401
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(new TokenVerifier(), AuthFilter.class)
                .addFilter(getAuthFilter())
                .authenticationProvider(customDaoAuthenticationProvider())
                .build();
    }

    private Filter getAuthFilter() {
        AuthFilter authFilter = new AuthFilter(authenticationManagerBuilder.getOrBuild(),
                                               handlerExceptionResolver);
        authFilter.setFilterProcessesUrl("/api/login");
        return authFilter;
    }

    @Bean
    public CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider(
                loginActivator, mailService, userService, userRepository);
        provider.setUserDetailsService(appUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
