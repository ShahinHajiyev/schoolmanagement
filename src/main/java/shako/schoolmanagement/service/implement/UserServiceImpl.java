package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dtomapper.StudentUserMapper;
import shako.schoolmanagement.dtomapper.UserMapper;
import shako.schoolmanagement.entity.Role;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.exception.StudentAlreadyExistsException;
import shako.schoolmanagement.exception.StudentNotActiveRequestException;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.repository.RoleRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.UserService;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudentUserMapper studentUserMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository, UserMapper userMapper, StudentUserMapper studentUserMapper, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.studentUserMapper = studentUserMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public void register(StudentUserDto studentUserDto) {

        Student studentFromDB = userRepository.findStudentByEmail(studentUserDto.getEmail());

        if (studentFromDB == null) {
            throw new StudentNotExistsException("Student does not exist");
        }

        //Optional<User> studentFromDB = userRepository.findByEmail(studentUserDto.getEmail());
        Student student = studentUserMapper.dtoToStudentEntity(studentUserDto);
        student.setNeptunCode(studentFromDB.getNeptunCode().toUpperCase());

        if (student.getEmail().equals(studentFromDB.getEmail()) &&
                student.getNeptunCode().equals(studentFromDB.getNeptunCode()) &&
                studentFromDB.getCreated() == null) {

            student.setUserId(studentFromDB.getUserId());
            student.setPassword(bCryptPasswordEncoder.encode(studentUserDto.getPassword()));
            student.setCreated(LocalDateTime.now());
            Role roles = roleRepository.findByRoleName("ROLE_USER");
            student.setRoles(Collections.singletonList(roles));
            studentRepository.save(student);
        }

        else {
            throw new StudentAlreadyExistsException("Student exists");
        }

    }

    @Override
    public Boolean isUserExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
