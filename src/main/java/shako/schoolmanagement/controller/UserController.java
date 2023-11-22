package shako.schoolmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dto.UserDto;
import shako.schoolmanagement.service.inter.StudentService;
import shako.schoolmanagement.service.inter.TeacherService;
import shako.schoolmanagement.service.inter.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {



    private final UserService userService;
    public final StudentService studentService;
    public final TeacherService teacherService;

    @Autowired
    public UserController(UserService userService, StudentService studentService, TeacherService teacherService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

/*    @PostMapping("/adduser")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto){
        userService.addUser(userDto);
        return ResponseEntity.ok("Student added successfully");
    }*/

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentUserDto studentUserDto){
        userService.register(studentUserDto);
        return ResponseEntity.ok("Student added successfully");
    }
}
