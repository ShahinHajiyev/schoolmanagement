package shako.schoolmanagement.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.*;
import shako.schoolmanagement.dtomapper.StudentUserMapper;
import shako.schoolmanagement.dtomapper.UserMapper;
import shako.schoolmanagement.entity.Role;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.exception.StudentAlreadyExistsException;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.repository.RoleRepository;
import shako.schoolmanagement.repository.SemesterRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.service.inter.UserService;
import shako.schoolmanagement.validator.LoginActivator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudentUserMapper studentUserMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final SemesterRepository semesterRepository;
    private final MailService mailService;
    private final LoginActivator loginActivator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository,
                           UserMapper userMapper, StudentUserMapper studentUserMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           RoleRepository roleRepository, SemesterRepository semesterRepository,
                           MailService mailService, LoginActivator loginActivator) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.studentUserMapper = studentUserMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.semesterRepository = semesterRepository;
        this.mailService = mailService;
        this.loginActivator = loginActivator;
    }

    @Override
    public void register(StudentUserDto studentUserDto) {
        log.info("Registering student with email: {}", studentUserDto.getEmail());
        Student studentFromDB = userRepository.findStudentByEmail(studentUserDto.getEmail());
        if (studentFromDB == null) {
            throw new StudentNotExistsException("Student does not exist");
        }
        Student student = studentUserMapper.dtoToStudentEntity(studentUserDto);
        student.setNeptunCode(studentFromDB.getNeptunCode().toUpperCase());

        if (student.getEmail().equals(studentFromDB.getEmail()) &&
                student.getNeptunCode().equals(studentFromDB.getNeptunCode()) &&
                studentFromDB.getIsActive() == null) {
            student.setUserId(studentFromDB.getUserId());
            student.setPassword(bCryptPasswordEncoder.encode(studentUserDto.getPassword()));
            student.setCreated(LocalDateTime.now());
            Role role = roleRepository.findByRoleName("ROLE_USER");
            student.setRoles(Collections.singletonList(role));
            student.setIsActive(false);
            student.setSemester(semesterRepository.findByName("first"));
            studentRepository.save(student);
            log.info("Student registered: {}", student.getNeptunCode());
        } else {
            throw new StudentAlreadyExistsException("Student exists");
        }
    }

    @Override
    public Boolean isUserExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void activateUser(ActivationCodeDto activationCode) {
        log.info("Activating user: {}", activationCode.getNeptunCode());
        User user = userRepository.findByNeptun(activationCode.getNeptunCode());
        if (!user.getActivationCode().equals(activationCode.getActivationCode())) {
            log.warn("Wrong activation code for user: {}", activationCode.getNeptunCode());
            throw new RuntimeException("Activation failed");
        }
        if (user.getActivationCodeExpiry() != null && user.getActivationCodeExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Expired activation code used for user: {}", activationCode.getNeptunCode());
            throw new RuntimeException("Activation code has expired");
        }
        user.setIsActive(true);
        userRepository.save(user);
        log.info("User {} activated", activationCode.getNeptunCode());
    }

    @Override
    public void saveActivationCode(User currentUser, String activationCode) {
        LocalDateTime expiry = LocalDateTime.now().plusHours(24);
        currentUser.setActivationCode(activationCode);
        currentUser.setActivationCodeExpiry(expiry);
        userRepository.saveActivationCode(currentUser.getNeptunCode(), activationCode, expiry);
    }

    @Override
    public void resendActivationCode(String neptunCode) {
        log.info("Resend activation code request for: {}", neptunCode);
        User user = userRepository.findByNeptun(neptunCode);
        if (user == null) {
            log.warn("Resend activation: user not found for neptunCode {}", neptunCode);
            return; // silent — avoid user enumeration
        }
        if (Boolean.TRUE.equals(user.getIsActive())) {
            log.info("Resend activation: user {} is already active", neptunCode);
            return;
        }
        String token = loginActivator.activationTokenGenerator();
        saveActivationCode(user, token);
        mailService.sendMail(user.getEmail(), "Activation code", token);
        log.info("Activation code resent to: {}", user.getEmail());
    }

    @Override
    public void forgotPassword(ForgotPasswordDto dto) {
        log.info("Forgot password request for email: {}", dto.getEmail());
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            user.setPasswordResetToken(token);
            user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            mailService.sendMail(user.getEmail(), "Password Reset",
                    "Your password reset token: " + token + " (valid for 1 hour)");
            log.info("Password reset token sent to: {}", dto.getEmail());
        });
        // Always return success to avoid user enumeration
    }

    @Override
    public void resetPassword(ResetPasswordDto dto) {
        log.info("Password reset attempt with token");
        User user = userRepository.findByPasswordResetToken(dto.getResetToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (user.getPasswordResetTokenExpiry() == null ||
                user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Expired password reset token used");
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);
        log.info("Password reset successfully for user: {}", user.getNeptunCode());
    }
}
