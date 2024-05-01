package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.entity.Semester;
import shako.schoolmanagement.service.inter.SemesterService;

import java.util.List;

@RestController
@RequestMapping("api/semester")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping("/getsemesters")
    public List<Semester> getSemester(){
        List<Semester> semesters = semesterService.getAllSemesters();
        return semesters;
    }
}
