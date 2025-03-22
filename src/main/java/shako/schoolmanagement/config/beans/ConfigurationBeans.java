package shako.schoolmanagement.config.beans;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shako.schoolmanagement.exception.RestError;

@Configuration
public class ConfigurationBeans {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.modulesToInstall(new JavaTimeModule());
    }

    @Bean
    public RestError restError(){
        return new RestError();
    }













}
