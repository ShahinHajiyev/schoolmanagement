package shako.schoolmanagement.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.validator.LoginActivator;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginActivator loginActivator;
    private  final MailService mailService;



    @Autowired
    public AppUserDetailsService(UserRepository userRepository, LoginActivator loginActivator, MailService mailService) {
        this.userRepository = userRepository;
        this.loginActivator = loginActivator;
        this.mailService = mailService;
    }




    @Override
    public UserDetails loadUserByUsername(String neptunCode) throws UsernameNotFoundException, DisabledException {


        Optional<User> user = userRepository.findByNeptunCode(neptunCode);


        user.orElseThrow(() -> new UsernameNotFoundException(String.format("UserName %s not found", neptunCode)));
        AppUserDetails returnedUser = user.map(AppUserDetails::new).get();


            return returnedUser;




    }

    }

