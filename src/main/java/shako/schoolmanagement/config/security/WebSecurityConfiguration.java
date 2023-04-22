package shako.schoolmanagement.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import shako.schoolmanagement.config.auth.AppUserDetailsService;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    //private final DataSource dataSource;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AppUserDetailsService appUserDetailsService;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //.addFilter(new AuthFilter(authenticationManagerBuilder.getOrBuild()))
                .addFilter(getAuthFilter())
                .addFilterAfter(new TokenVerifier(), AuthFilter.class)
                .authorizeRequests()
                //http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                                .antMatchers(HttpMethod.GET, "index","/css","/js").permitAll()
                                .antMatchers("/api/student/addstudent").permitAll()
                                .antMatchers("/api/course/**").hasAnyRole("USER","ADMIN")
                                .antMatchers("/api/enrollment/**").hasRole("ADMIN")
                                .anyRequest().authenticated();
        return http.build();
    }

    private Filter getAuthFilter() {

        AuthFilter authFilter = new AuthFilter(authenticationManagerBuilder.getOrBuild());
        authFilter.setFilterProcessesUrl("/api/login");
        return authFilter;
    }


    /*        @Bean
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
    }*/

/*    @Bean
    public DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> configure(AuthenticationManagerBuilder auth) throws Exception {
        return auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder);
    }*/

    /*    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }*/



/*    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }*/



}
