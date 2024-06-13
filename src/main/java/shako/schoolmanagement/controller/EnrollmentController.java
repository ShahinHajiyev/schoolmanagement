package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.AddEnrollmentDto;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.exception.EnrollmentOutOfLimitException;
import shako.schoolmanagement.exception.EnrollmentOutOfTimeException;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/testdropdown")
    public List<String> getEnrollmentDropdownMenu(){
        List<String> dropdowns = new ArrayList<>();
        dropdowns.add("menu-1");
        dropdowns.add("menu-2");
        dropdowns.add("menu-3");
        dropdowns.add("menu-4");
        dropdowns.add("menu-5");

        return dropdowns;
    }

    @GetMapping("/getallenrollments")
    public List<EnrollmentDto> getAllEnrollment(){
        return enrollmentService.getAllEnrollments();
    }

//    @PostMapping("/addenrollment")
//    public ResponseEntity addEnrollment(@RequestBody EnrollmentDto enrollmentDto){
//        enrollmentService.addEnrollment(enrollmentDto);
//        return ResponseEntity.ok("Student enrolled to the course successfully! ");
//    }

    //To Do  make dto and service same as frontend

    @PostMapping("/addenrollment")
    public ResponseEntity<?> addEnrollment(@RequestBody AddEnrollmentDto enrollmentDto){
        try {
            enrollmentService.addEnrollment(enrollmentDto);
            return ResponseEntity.ok("Student enrolled to the course successfully! ");
        }catch (EnrollmentOutOfLimitException | EnrollmentOutOfTimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteenrollment/{id}")
    public ResponseEntity<?> deleteenrollment(@PathVariable("id") Integer enrollmentId){
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok("Student deleted successfully! ");
    }
}
