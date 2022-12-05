package shako.schoolmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.service.inter.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {


    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/addstudent")
    public ResponseEntity addStudent(@RequestBody StudentUserDto studentUserDto){

        studentService.addStudent(studentUserDto);
        return ResponseEntity.ok("Student added successfully");
    }

}
