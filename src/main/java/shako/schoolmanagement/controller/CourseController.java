package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.CourseDto;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.entity.Semester;
import shako.schoolmanagement.repository.SemesterRepository;
import shako.schoolmanagement.service.inter.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;
    private final SemesterRepository semesterRepository;

    @GetMapping("/getcourses")
    public List<CourseDto> getCourses(){
        return courseService.getCourses();
    }

    @GetMapping("/availablecourses")
    public List<CourseDto> getAvailableCourses(){
        return courseService.getAvailableCourses();
    }


}
