package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.dtomapper.AdminStudentMapper;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.Teacher;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.exception.EmailCannotBeEmptyException;
import shako.schoolmanagement.exception.UserTypeCannotBeEmptyException;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.repository.TeacherRepository;
import shako.schoolmanagement.service.inter.AdminService;
import shako.schoolmanagement.service.inter.StudentService;
import shako.schoolmanagement.service.inter.UserService;

@Service
public class AdminServiceImpl implements AdminService {


    private final AdminStudentMapper adminStudentMapper;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserService userService;



    @Autowired
    public AdminServiceImpl(AdminStudentMapper adminStudentMapper,
                            StudentRepository studentRepository, TeacherRepository teacherRepository,
                            UserService userService) {
        this.adminStudentMapper = adminStudentMapper;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userService = userService;
    }


    @Override
    public void addUserByAdmin(AdminStudentDto adminStudentDto) {

        if (adminStudentDto.getEmail() == null) {
            throw new EmailCannotBeEmptyException("Email is empty");
        }

        if (userService.isUserExistsByEmail(adminStudentDto.getEmail())) {
            throw new IllegalArgumentException("The email already taken, Check carefully!");
        }

        User user;

        if (adminStudentDto.getSelectedUser().equals("Student")) {
            user = new Student();
            persistUser(user, adminStudentDto, studentRepository);
       }
        else if (adminStudentDto.getSelectedUser().equals("Teacher")) {
            user = new Teacher();
            persistUser(user, adminStudentDto, teacherRepository);
        }
        else
            throw new UserTypeCannotBeEmptyException("No user type selected");


    }

    public void persistUser(User user, AdminStudentDto dto, JpaRepository repository){
        user.setNeptunCode(dto.getNeptunCode().toUpperCase());
        user.setEmail(dto.getEmail());
        repository.save((User)user);

    }
}
