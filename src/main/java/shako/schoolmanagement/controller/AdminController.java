package shako.schoolmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.AddCourseDto;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.dto.AdminUserListDto;
import shako.schoolmanagement.entity.Program;
import shako.schoolmanagement.repository.ProgramRepository;
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

    @Autowired
    private ProgramRepository programRepository;

    @PostMapping("/addStudentByAdmin")
    public ResponseEntity<Void> addStudent(@Valid @RequestBody AdminStudentDto adminStudentDto) {
        log.info("POST /admin/addStudentByAdmin — neptunCode: {}", adminStudentDto.getNeptunCode());
        adminService.addUserByAdmin(adminStudentDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public List<AdminUserListDto> getAllUsers() {
        log.info("GET /admin/users");
        return adminService.getAllUsers();
    }

    @PostMapping("/addcourse")
    public ResponseEntity<Void> addCourse(@Valid @RequestBody AddCourseDto addCourseDto) {
        log.info("POST /admin/addcourse — name: '{}'", addCourseDto.getCourseName());
        courseService.addCourse(addCourseDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        log.info("DELETE /admin/course/{}", id);
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{neptunCode}/unblock-activation")
    public ResponseEntity<Void> unblockActivation(@PathVariable String neptunCode) {
        log.info("POST /admin/users/{}/unblock-activation", neptunCode);
        adminService.unblockActivation(neptunCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/programs")
    public List<Program> getPrograms() {
        log.info("GET /admin/programs");
        return programRepository.findAll();
    }
}
