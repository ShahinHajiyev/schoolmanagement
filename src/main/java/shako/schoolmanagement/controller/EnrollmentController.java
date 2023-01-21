package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/getallenrollments")
    public List<EnrollmentDto> getAllEnrollment(){
        return enrollmentService.getAllEnrollments();
    }

    @PostMapping("/addenrollment")
    public ResponseEntity addEnrollment(@RequestBody EnrollmentDto enrollmentDto){
        enrollmentService.addEnrollment(enrollmentDto);
        return ResponseEntity.ok("Student enrolled to the course successfully! ");
    }

    @DeleteMapping("/deleteenrollment/{id}")
    public ResponseEntity deleteenrollment(@PathVariable("id") Integer enrollmentId){
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok("Student deleted successfully! ");
    }
}
