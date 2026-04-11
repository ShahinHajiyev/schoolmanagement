package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.*;
import shako.schoolmanagement.service.inter.CourseService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/getcourses")
    public List<CourseDto> getCourses() {
        log.info("GET /course/getcourses");
        return courseService.getCourses();
    }

    @GetMapping("/availablecourses")
    public List<CourseDto> getAvailableCourses() {
        log.info("GET /course/availablecourses");
        return courseService.getAvailableCourses();
    }

    @GetMapping("/getcourse/{courseId}")
    public CourseDto getCourseByCourseId(@PathVariable int courseId) {
        log.info("GET /course/getcourse/{}", courseId);
        return courseService.getCourseByCourseId(courseId);
    }

    @GetMapping("/{courseId}/students")
    public List<StudentDto> getStudentsByCourseId(@PathVariable int courseId) {
        log.info("GET /course/{}/students", courseId);
        return courseService.getStudentsByCourseId(courseId);
    }

    @PostMapping("/addcourse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addCourse(@RequestBody @Valid AddCourseDto dto) {
        log.info("POST /course/addcourse");
        courseService.addCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Course added successfully");
    }

    @DeleteMapping("/deletecourse/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCourse(@PathVariable int courseId) {
        log.info("DELETE /course/deletecourse/{}", courseId);
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("Course deleted successfully");
    }

    // ── Course Details ────────────────────────────────────────────────────────

    @GetMapping("/{courseId}/details")
    public ResponseEntity<CourseDetailsDto> getCourseDetails(@PathVariable int courseId) {
        log.info("GET /course/{}/details", courseId);
        CourseDetailsDto details = courseService.getCourseDetails(courseId);
        if (details == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(details);
    }

    @PutMapping("/{courseId}/details")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<CourseDetailsDto> saveCourseDetails(@PathVariable int courseId,
                                                              @RequestBody @Valid CourseDetailsDto dto) {
        log.info("PUT /course/{}/details", courseId);
        return ResponseEntity.ok(courseService.saveCourseDetails(courseId, dto));
    }

    // ── Course Schedule ───────────────────────────────────────────────────────

    @GetMapping("/{courseId}/schedule")
    public List<CourseScheduleDto> getScheduleForCourse(@PathVariable int courseId) {
        log.info("GET /course/{}/schedule", courseId);
        return courseService.getScheduleForCourse(courseId);
    }

    @PostMapping("/{courseId}/schedule")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<CourseScheduleDto> addScheduleEntry(@PathVariable int courseId,
                                                              @RequestBody @Valid CourseScheduleDto dto) {
        log.info("POST /course/{}/schedule", courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addScheduleEntry(courseId, dto));
    }

    @DeleteMapping("/schedule/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<String> deleteScheduleEntry(@PathVariable int scheduleId) {
        log.info("DELETE /course/schedule/{}", scheduleId);
        courseService.deleteScheduleEntry(scheduleId);
        return ResponseEntity.ok("Schedule entry deleted");
    }
}
