package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.dtomapper.AdminStudentMapper;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.service.inter.AdminService;
import shako.schoolmanagement.service.inter.StudentService;

@Service
public class AdminServiceImpl implements AdminService {


    private final AdminStudentMapper adminStudentMapper;
    private final StudentRepository studentRepository;
    private final StudentService studentService;



    @Autowired
    public AdminServiceImpl(AdminStudentMapper adminStudentMapper,
                            StudentRepository studentRepository,
                            StudentService studentService) {
        this.adminStudentMapper = adminStudentMapper;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }


    @Override
    public void addStudentByAdmin(AdminStudentDto adminStudentDto) {

        Student student = adminStudentMapper.dtoToStudentEntity(adminStudentDto);
        if (studentService.isUserExistsByEmail(adminStudentDto.getEmail())) {
            throw new IllegalArgumentException("The email already taken, Check carefully!");
        }
        studentRepository.save(student);
    }
}
