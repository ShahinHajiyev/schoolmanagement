package shako.schoolmanagement.config.beans;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shako.schoolmanagement.config.security.AuthFilter;

@Configuration
public class ConfigurationBeans {



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    };

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }









}
