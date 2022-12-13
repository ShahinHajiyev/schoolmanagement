package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/getenrollment")
    public List<Enrollment> getEnrollment(){
        return enrollmentService.getEnrollments();
    }


}
