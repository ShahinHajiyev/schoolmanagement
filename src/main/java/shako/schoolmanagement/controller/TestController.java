package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.service.StudentService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    @Autowired
    private final StudentService studentService;

    @PostMapping("/addstudent")
    public ResponseEntity addStudent(@RequestBody Student student){

        studentService.addStudent(student);
        return ResponseEntity.ok("Student added successfully");
    }

}
