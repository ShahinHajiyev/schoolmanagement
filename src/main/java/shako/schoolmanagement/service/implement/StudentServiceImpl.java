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


    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public List<Student> getAll() {
        List<Student> students = studentRepository.findAll();
        return students;
    }

}
