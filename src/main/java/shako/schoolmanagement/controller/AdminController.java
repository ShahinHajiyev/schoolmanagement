package shako.schoolmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.exception.EmailCannotBeEmptyException;
import shako.schoolmanagement.service.inter.AdminService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/addStudentByAdmin")
    public ResponseEntity addStudent(@Valid @RequestBody AdminStudentDto adminStudentDto){
        try {
            adminService.addStudentByAdmin(adminStudentDto);
            return ResponseEntity.ok("Student added successfully");
        } catch (EmailCannotBeEmptyException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
