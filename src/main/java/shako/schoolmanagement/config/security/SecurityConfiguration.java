package shako.schoolmanagement.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import shako.schoolmanagement.config.auth.AppUserDetailsService;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {

    private final PasswordEncoder passwordEncoder;
    private final AppUserDetailsService appUserDetailsService;
    private final DataSource dataSource;


    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder, AppUserDetailsService appUserDetailsService, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.appUserDetailsService = appUserDetailsService;
        this.dataSource = dataSource;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(appUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

/*    @Bean
    public DaoAuthenticationConfigurer<AuthenticationManagerBuilder, AppUserDetailsService> configure(AuthenticationManagerBuilder auth) throws Exception {
        return auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder);
    }*/



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests( configurer ->
                {
                    try {
                        configurer
                                .antMatchers("/", "index","/css","/js").permitAll()
                                //.antMatchers("/api/**").hasRole("ADMIN")
                                .antMatchers("/api/student/addstudent").permitAll()
                                .anyRequest()
                                .authenticated()
                                .and()
                                .formLogin()
                                    //.loginPage("/login").permitAll()
                                .and()
                                .rememberMe()
                                      .tokenRepository(persistentTokenRepository())
                                      .tokenValiditySeconds(SecurityConstants.REMEMBER_ME_EXPIRATION_TIME)
                                .and()
                                .logout()
                                    .logoutUrl("/logout");
                                //.defaultSuccessUrl()

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        httpSecurity.cors();
        httpSecurity.csrf().disable();
        //httpSecurity.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        return httpSecurity.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }


}
