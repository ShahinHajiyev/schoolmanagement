package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.UserDto;
import shako.schoolmanagement.dtomapper.UserMapper;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void addUser(UserDto userDto) {
    User user = userMapper.dtoToUserEntity(userDto);
    user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
    user.setCreated(LocalDateTime.now());
    userRepository.save(user);
    }
}
