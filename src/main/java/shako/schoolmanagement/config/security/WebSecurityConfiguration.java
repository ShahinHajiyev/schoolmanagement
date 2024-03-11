package shako.schoolmanagement.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;
import shako.schoolmanagement.config.auth.AppUserDetails;
import shako.schoolmanagement.config.auth.AppUserDetailsService;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.validator.LoginValidator;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity/*(debug = true)*/
//@RequiredArgsConstructor
public class WebSecurityConfiguration {



    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private  HandlerExceptionResolver handlerExceptionResolver;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private  AppUserDetailsService appUserDetailsService;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private LoginValidator loginValidator;


    @Autowired
    public AccountStatusCheckerFilter statusCheckerFilter(){
        return new AccountStatusCheckerFilter(userRepository,
                                              handlerExceptionResolver,
                                              mailService,
                                              loginValidator);
    }


    @Autowired
    void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder);
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        return http.cors().and()
                .authorizeRequests(customizer ->
                customizer
                .antMatchers(HttpMethod.GET, "index", "/css", "/js","/error").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/course/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/enrollment/**").hasRole("ADMIN")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                )
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new TokenVerifier(handlerExceptionResolver), AuthFilter.class)
                .addFilterBefore(statusCheckerFilter(), AuthFilter.class)
                .addFilter(getAuthFilter()).exceptionHandling().authenticationEntryPoint(this.authEntryPoint).and()  //this auth can also be deleted


                        .authenticationProvider(daoAuthenticationProvider())

        .build();






    }

    private Filter getAuthFilter() {

        AuthFilter authFilter = new AuthFilter( authenticationManagerBuilder.getOrBuild(), handlerExceptionResolver);
        authFilter.setFilterProcessesUrl("/api/login");

        return authFilter;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


}
