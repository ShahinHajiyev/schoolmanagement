package shako.schoolmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.service.inter.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/addStudentByAdmin")
    public ResponseEntity addStudent(@RequestBody AdminStudentDto adminStudentDto){
        adminService.addStudentByAdmin(adminStudentDto);
        return ResponseEntity.ok("Student added successfully");
    }
}
