package shako.schoolmanagement.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.dto.*;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.Teacher;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.exception.StudentAlreadyExistsException;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.repository.RoleRepository;
import shako.schoolmanagement.repository.SemesterRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.repository.TeacherRepository;
import shako.schoolmanagement.repository.UserRepository;
import shako.schoolmanagement.service.inter.MailService;
import shako.schoolmanagement.service.inter.UserService;
import shako.schoolmanagement.validator.LoginActivator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final SemesterRepository semesterRepository;
    private final MailService mailService;
    private final LoginActivator loginActivator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           StudentRepository studentRepository,
                           TeacherRepository teacherRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           RoleRepository roleRepository,
                           SemesterRepository semesterRepository,
                           MailService mailService,
                           LoginActivator loginActivator) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.semesterRepository = semesterRepository;
        this.mailService = mailService;
        this.loginActivator = loginActivator;
    }

    @Transactional
    @Override
    public void register(StudentUserDto dto) {
        log.info("Registering user with email: {}", dto.getEmail());
        User userFromDB = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new StudentNotExistsException("User does not exist"));

        if (!dto.getEmail().equals(userFromDB.getEmail()) ||
                !dto.getNeptunCode().toUpperCase().equals(userFromDB.getNeptunCode()) ||
                userFromDB.getIsActive() != null) {
            throw new StudentAlreadyExistsException("User already registered");
        }

        User savedUser;
        if (Boolean.TRUE.equals(userFromDB.getIsTeacher())) {
            Teacher teacher = teacherRepository.findById(userFromDB.getUserId())
                    .orElseThrow(() -> new StudentNotExistsException("Teacher record not found"));
            applyRegistrationFields(teacher, dto);
            teacher.setRoles(Collections.singletonList(roleRepository.findByRoleName("ROLE_USER")));
            teacherRepository.save(teacher);
            savedUser = teacher;
            log.info("Teacher registered: {}", teacher.getNeptunCode());
        } else {
            Student student = studentRepository.findById(userFromDB.getUserId())
                    .orElseThrow(() -> new StudentNotExistsException("Student record not found"));
            applyRegistrationFields(student, dto);
            student.setRoles(Collections.singletonList(roleRepository.findByRoleName("ROLE_USER")));
            student.setSemester(semesterRepository.findByName("first"));
            studentRepository.save(student);
            savedUser = student;
            log.info("Student registered: {}", student.getNeptunCode());
        }
        String token = loginActivator.activationTokenGenerator();
        saveActivationCode(savedUser, token);
        mailService.sendMail(savedUser.getEmail(), "Activation code", token);
        log.info("Activation code sent to: {}", savedUser.getEmail());
    }

    private void applyRegistrationFields(User user, StudentUserDto dto) {
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setIsActive(false);
    }

    @Override
    public Boolean isUserExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void activateUser(ActivationCodeDto activationCode) {
        log.info("Activating user: {}", activationCode.getNeptunCode());
        User user = userRepository.findByNeptunCode(activationCode.getNeptunCode())
                .orElseThrow(() -> new StudentNotExistsException("User not found"));

        // Permanent block — requires admin intervention
        if (user.getActivationBlockPhase() == 3) {
            log.warn("Permanently blocked activation attempt for: {}", activationCode.getNeptunCode());
            throw new IllegalArgumentException("Account is permanently locked. Please contact admin.");
        }

        // Active temporary block
        if (user.getActivationBlockedUntil() != null
                && LocalDateTime.now().isBefore(user.getActivationBlockedUntil())) {
            log.warn("Blocked activation attempt for: {} until {}", activationCode.getNeptunCode(), user.getActivationBlockedUntil());
            throw new IllegalArgumentException("Too many failed attempts. Try again after " + user.getActivationBlockedUntil());
        }

        // Block window expired — reset attempt count for new window
        if (user.getActivationBlockedUntil() != null) {
            userRepository.updateActivationLockout(activationCode.getNeptunCode(), 0, null, user.getActivationBlockPhase());
            user.setActivationAttempts(0);
            user.setActivationBlockedUntil(null);
        }

        if (user.getActivationCodeExpiry() != null && user.getActivationCodeExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Expired activation code used for user: {}", activationCode.getNeptunCode());
            throw new IllegalArgumentException("Activation code has expired");
        }

        if (!activationCode.getActivationCode().equals(user.getActivationCode())) {
            log.warn("Wrong activation code for user: {}", activationCode.getNeptunCode());
            int attempts = user.getActivationAttempts() + 1;
            int phase = user.getActivationBlockPhase();
            LocalDateTime blockedUntil = null;

            if (attempts >= 3) {
                switch (phase) {
                    case 0 -> { blockedUntil = LocalDateTime.now().plusMinutes(30); phase = 1; }
                    case 1 -> { blockedUntil = LocalDateTime.now().plusHours(2);    phase = 2; }
                    case 2 ->   phase = 3;
                }
                attempts = 0;
                log.warn("Activation lockout triggered for: {} — phase now {}", activationCode.getNeptunCode(), phase);
            }
            userRepository.updateActivationLockout(activationCode.getNeptunCode(), attempts, blockedUntil, phase);
            throw new IllegalArgumentException("Invalid activation code");
        }

        // Correct code — activate and reset all lockout state
        userRepository.activateAndClearLockout(activationCode.getNeptunCode());
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
        Optional<User> userOpt = userRepository.findByNeptunCode(neptunCode);
        if (userOpt.isEmpty()) {
            log.warn("Resend activation: user not found for neptunCode {}", neptunCode);
            return; // silent — avoid user enumeration
        }
        User user = userOpt.get();
        if (Boolean.TRUE.equals(user.getIsActive())) {
            log.info("Resend activation: user {} is already active", neptunCode);
            return;
        }
        if (user.getActivationBlockPhase() == 3) {
            log.warn("Resend blocked — permanently locked account: {}", neptunCode);
            throw new IllegalArgumentException("Account is permanently locked. Please contact admin.");
        }
        if (user.getActivationBlockedUntil() != null
                && LocalDateTime.now().isBefore(user.getActivationBlockedUntil())) {
            log.warn("Resend blocked — account locked until {} for: {}", user.getActivationBlockedUntil(), neptunCode);
            throw new IllegalArgumentException("Account is temporarily locked. Try again after " + user.getActivationBlockedUntil());
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
