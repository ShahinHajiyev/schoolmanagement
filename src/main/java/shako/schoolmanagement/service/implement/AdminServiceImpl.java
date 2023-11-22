package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.dtomapper.AdminStudentMapper;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.service.inter.AdminService;
import shako.schoolmanagement.service.inter.StudentService;
import shako.schoolmanagement.service.inter.UserService;

@Service
public class AdminServiceImpl implements AdminService {


    private final AdminStudentMapper adminStudentMapper;
    private final StudentRepository studentRepository;
    private final UserService userService;



    @Autowired
    public AdminServiceImpl(AdminStudentMapper adminStudentMapper,
                            StudentRepository studentRepository,
                            UserService userService) {
        this.adminStudentMapper = adminStudentMapper;
        this.studentRepository = studentRepository;
        this.userService = userService;
    }


    @Override
    public void addStudentByAdmin(AdminStudentDto adminStudentDto) {

        Student student = adminStudentMapper.dtoToStudentEntity(adminStudentDto);
        if (userService.isUserExistsByEmail(adminStudentDto.getEmail())) {
            throw new IllegalArgumentException("The email already taken, Check carefully!");
        }
        studentRepository.save(student);
    }
}
