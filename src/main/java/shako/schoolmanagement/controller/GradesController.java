package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dto.UpdateGradeDto;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradesController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/my")
    public List<EnrollmentDto> getMyGrades() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String neptunCode = authentication.getName();
        log.info("GET /grades/my — student: {}", neptunCode);
        return enrollmentService.getMyEnrollments(neptunCode);
    }

    @GetMapping("/course/{courseId}")
    public List<EnrollmentDto> getGradesByCourse(@PathVariable int courseId) {
        log.info("GET /grades/course/{}", courseId);
        return enrollmentService.getEnrollmentsByCourseId(courseId);
    }

    @PutMapping("/{enrollmentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateGrade(@PathVariable int enrollmentId, @RequestBody UpdateGradeDto updateGradeDto) {
        log.info("PUT /grades/{} — grade: {}", enrollmentId, updateGradeDto.getGrade());
        enrollmentService.updateGrade(enrollmentId, updateGradeDto.getGrade());
        return ResponseEntity.ok("Grade updated successfully");
    }
}
