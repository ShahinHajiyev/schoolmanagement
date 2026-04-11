package shako.schoolmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.AddCourseDto;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.dto.AdminUserListDto;
import shako.schoolmanagement.service.inter.AdminService;
import shako.schoolmanagement.service.inter.CourseService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/addStudentByAdmin")
    public ResponseEntity<String> addStudent(@Valid @RequestBody AdminStudentDto adminStudentDto) {
        log.info("POST /admin/addStudentByAdmin — neptunCode: {}", adminStudentDto.getNeptunCode());
        adminService.addUserByAdmin(adminStudentDto);
        return ResponseEntity.ok("Student added successfully");
    }

    @GetMapping("/users")
    public List<AdminUserListDto> getAllUsers() {
        log.info("GET /admin/users");
        return adminService.getAllUsers();
    }

    @PostMapping("/addcourse")
    public ResponseEntity<String> addCourse(@Valid @RequestBody AddCourseDto addCourseDto) {
        log.info("POST /admin/addcourse — name: '{}'", addCourseDto.getCourseName());
        courseService.addCourse(addCourseDto);
        return ResponseEntity.ok("Course added successfully");
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        log.info("DELETE /admin/course/{}", id);
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully");
    }
}
