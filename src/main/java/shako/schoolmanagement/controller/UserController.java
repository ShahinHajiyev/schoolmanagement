package shako.schoolmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.ActivationCodeDto;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dto.UserDto;
import shako.schoolmanagement.exception.StudentAlreadyExistsException;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.service.inter.StudentService;
import shako.schoolmanagement.service.inter.TeacherService;
import shako.schoolmanagement.service.inter.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    public final StudentService studentService;
    public final TeacherService teacherService;

    public final UserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, StudentService studentService, TeacherService teacherService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userDetailsService = userDetailsService;
    }

/*    @PostMapping("/adduser")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto){
        userService.addUser(userDto);
        return ResponseEntity.ok("Student added successfully");
    }*/

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentUserDto studentUserDto){
        try {
            userService.register(studentUserDto);
            return ResponseEntity.ok("Student added successfully");
        }
        catch (StudentNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (StudentAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Student already exists");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping("/activate")
    public ResponseEntity<?> validate(@RequestBody @Valid ActivationCodeDto activationCode) {
        this.userService.activateUser(activationCode);
        try {
            return ResponseEntity.ok("Activation successful");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    
}
