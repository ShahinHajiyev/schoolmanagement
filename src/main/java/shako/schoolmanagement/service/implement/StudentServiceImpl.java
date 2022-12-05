package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dtomapper.StudentUserMapper;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.service.inter.StudentService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StudentUserMapper studentUserMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              StudentUserMapper studentUserMapper) {
        this.studentRepository = studentRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.studentUserMapper = studentUserMapper;
    }


    @Override
    public void addStudent(StudentUserDto studentUserDto) {
        /*Optional<Integer> studentId = Optional.of(studentUserDto.getUserId());
        studentId.ifPresent(message -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Student is already existed");});*/
        Student student = studentUserMapper.dtoToStudentEntity(studentUserDto);
        student.setPassword(bCryptPasswordEncoder.encode(studentUserDto.getPassword()));
        student.setCreated(LocalDateTime.now());
        studentRepository.save(student);
    }
}
