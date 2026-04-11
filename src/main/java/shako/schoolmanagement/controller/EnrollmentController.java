package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.AddEnrollmentDto;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.exception.JsonResponse;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/testdropdown")
    public List<String> getEnrollmentDropdownMenu() {
        List<String> dropdowns = new ArrayList<>();
        dropdowns.add("menu-1");
        dropdowns.add("menu-2");
        dropdowns.add("menu-3");
        dropdowns.add("menu-4");
        dropdowns.add("menu-5");
        return dropdowns;
    }

    @GetMapping("/getallenrollments")
    public List<EnrollmentDto> getAllEnrollment() {
        log.info("GET /getallenrollments");
        return enrollmentService.getAllEnrollments();
    }

    @PostMapping("/addenrollment")
    public ResponseEntity<JsonResponse> addEnrollment(@RequestBody AddEnrollmentDto enrollmentDto) {
        log.info("POST /addenrollment — courseId: {}, student: {}", enrollmentDto.getCourseId(), enrollmentDto.getNeptunCode());
        enrollmentService.addEnrollment(enrollmentDto);
        return ResponseEntity.ok(new JsonResponse("Enrolled successfully"));
    }

    @DeleteMapping("/deleteenrollment/{id}")
    public ResponseEntity<?> deleteenrollment(@PathVariable("id") Integer enrollmentId) {
        log.info("DELETE /deleteenrollment/{}", enrollmentId);
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok("Student deleted successfully! ");
    }

    @GetMapping("/myenrollments")
    public List<EnrollmentDto> getMyEnrollments() {
        String neptunCode = getLoggedInNeptunCode();
        log.info("GET /myenrollments — student: {}", neptunCode);
        return enrollmentService.getMyEnrollments(neptunCode);
    }

    @GetMapping("/isenrolled/{courseId}")
    public boolean isEnrolled(@PathVariable int courseId) {
        String neptunCode = getLoggedInNeptunCode();
        log.info("GET /isenrolled/{} — student: {}", courseId, neptunCode);
        return enrollmentService.isEnrolled(neptunCode, courseId);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> unregister(@PathVariable int courseId) {
        String neptunCode = getLoggedInNeptunCode();
        log.info("DELETE /enrollment/{} — student: {}", courseId, neptunCode);
        enrollmentService.unregisterFromCourse(neptunCode, courseId);
        return ResponseEntity.ok("Unregistered successfully");
    }

    private String getLoggedInNeptunCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
