package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dtomapper.StudentUserMapper;
import shako.schoolmanagement.entity.Role;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.repository.RoleRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.StudentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StudentUserMapper studentUserMapper;

    private final UserRepository userRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              RoleRepository roleRepository,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              StudentUserMapper studentUserMapper, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.studentUserMapper = studentUserMapper;
        this.userRepository = userRepository;
    }


    @Override
    public void addStudent(StudentUserDto studentUserDto) {

        Student student = studentUserMapper.dtoToStudentEntity(studentUserDto);
        /*//boolean isUserExists=userRepository.findByEmail(studentUserDto.getEmail()).isPresent();
        //if (isUserExists) {
        if (isUserExistsByEmail(studentUserDto.getEmail())) {
                    throw new IllegalStateException("email is already taken");
        }*/
        student.setPassword(bCryptPasswordEncoder.encode(studentUserDto.getPassword()));
        student.setCreated(LocalDateTime.now());
        Role roles = roleRepository.findByRoleName("ROLE_USER");
        student.setRoles(Collections.singletonList(roles));
        studentRepository.save(student);
    }

    @Override
    public Boolean isUserExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }



    @Override
    public List<Student> getAll() {
        List<Student> students = studentRepository.findAll();
        return students;
    }

}
