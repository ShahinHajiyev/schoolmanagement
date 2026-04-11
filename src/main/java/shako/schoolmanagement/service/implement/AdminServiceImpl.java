package shako.schoolmanagement.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.dto.AdminUserListDto;
import shako.schoolmanagement.dtomapper.AdminStudentMapper;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.Teacher;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.exception.EmailCannotBeEmptyException;
import shako.schoolmanagement.exception.UserTypeCannotBeEmptyException;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.repository.TeacherRepository;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.AdminService;
import shako.schoolmanagement.service.inter.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminStudentMapper adminStudentMapper;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(AdminStudentMapper adminStudentMapper,
                            StudentRepository studentRepository,
                            TeacherRepository teacherRepository,
                            UserService userService,
                            UserRepository userRepository) {
        this.adminStudentMapper = adminStudentMapper;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void addUserByAdmin(AdminStudentDto adminStudentDto) {
        log.info("Admin adding user — neptunCode: {}, type: {}", adminStudentDto.getNeptunCode(), adminStudentDto.getSelectedUser());

        if (adminStudentDto.getEmail() == null) {
            log.warn("Email is null for neptunCode: {}", adminStudentDto.getNeptunCode());
            throw new EmailCannotBeEmptyException("Email is empty");
        }
        if (userService.isUserExistsByEmail(adminStudentDto.getEmail())) {
            log.warn("Email already taken: {}", adminStudentDto.getEmail());
            throw new IllegalArgumentException("The email already taken, Check carefully!");
        }

        User user;
        if (adminStudentDto.getSelectedUser().equals("Student")) {
            user = new Student();
            persistUser(user, adminStudentDto, studentRepository);
        } else if (adminStudentDto.getSelectedUser().equals("Teacher")) {
            user = new Teacher();
            persistUser(user, adminStudentDto, teacherRepository);
        } else {
            log.warn("Unknown user type: {}", adminStudentDto.getSelectedUser());
            throw new UserTypeCannotBeEmptyException("No user type selected");
        }
        log.info("User {} ({}) added by admin", adminStudentDto.getNeptunCode(), adminStudentDto.getSelectedUser());
    }

    @Override
    public List<AdminUserListDto> getAllUsers() {
        log.debug("Fetching all users for admin");
        List<User> users = userRepository.findAll();
        log.debug("Found {} users", users.size());
        return users.stream().map(user -> {
            String role = user.getRoles().isEmpty() ? "UNKNOWN" : user.getRoles().get(0).getRoleName();
            return new AdminUserListDto(user.getNeptunCode(), user.getEmail(), role);
        }).collect(Collectors.toList());
    }

    public void persistUser(User user, AdminStudentDto dto, JpaRepository repository) {
        user.setNeptunCode(dto.getNeptunCode().toUpperCase());
        user.setEmail(dto.getEmail());
        repository.save((User) user);
        log.debug("User persisted — neptunCode: {}", dto.getNeptunCode());
    }
}
