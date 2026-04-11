package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.*;
import shako.schoolmanagement.service.inter.TeacherService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherDto> getAllTeachers() {
        log.info("GET /teacher");
        return teacherService.getAllTeachers();
    }

    @GetMapping("/mycourses")
    @PreAuthorize("hasRole('TEACHER')")
    public List<CourseDto> getMyCourses() {
        String neptunCode = getCurrentNeptunCode();
        log.info("GET /teacher/mycourses — teacher: {}", neptunCode);
        return teacherService.getMyCourses(neptunCode);
    }

    @GetMapping("/mycourses/{courseId}/students")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public List<StudentDto> getStudentsForCourse(@PathVariable int courseId) {
        String neptunCode = getCurrentNeptunCode();
        log.info("GET /teacher/mycourses/{}/students — teacher: {}", courseId, neptunCode);
        return teacherService.getMyStudents(neptunCode);
    }

    @GetMapping("/mycourses/{courseId}/enrollments")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public List<EnrollmentDto> getCourseEnrollments(@PathVariable int courseId) {
        String neptunCode = getCurrentNeptunCode();
        log.info("GET /teacher/mycourses/{}/enrollments — teacher: {}", courseId, neptunCode);
        return teacherService.getCourseEnrollments(neptunCode, courseId);
    }

    private String getCurrentNeptunCode() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
