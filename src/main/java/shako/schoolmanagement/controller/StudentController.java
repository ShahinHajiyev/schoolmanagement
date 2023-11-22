package shako.schoolmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.service.inter.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {


    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }



    @GetMapping
    public List<Student> getAll(){
        return studentService.getAll();
    }

}
