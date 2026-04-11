package shako.schoolmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dto.GpaDto;
import shako.schoolmanagement.dto.StudentResponseDto;
import shako.schoolmanagement.dto.TranscriptItemDto;
import shako.schoolmanagement.service.inter.StudentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Page<StudentResponseDto> getAll(@PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /student");
        return studentService.getAll(pageable);
    }

    @GetMapping("/all")
    public Page<StudentResponseDto> getAllStudents(@PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /student/all");
        return studentService.getAll(pageable);
    }

    @GetMapping("/{neptunCode}/enrollments")
    public List<EnrollmentDto> getStudentEnrollments(@PathVariable String neptunCode) {
        log.info("GET /student/{}/enrollments", neptunCode);
        return studentService.getEnrollmentsByNeptunCode(neptunCode);
    }

    @GetMapping("/{neptunCode}/gpa")
    public GpaDto getGpa(@PathVariable String neptunCode) {
        log.info("GET /student/{}/gpa", neptunCode);
        return studentService.getGpa(neptunCode);
    }

    @GetMapping("/{neptunCode}/transcript")
    public List<TranscriptItemDto> getTranscript(@PathVariable String neptunCode) {
        log.info("GET /student/{}/transcript", neptunCode);
        return studentService.getTranscript(neptunCode);
    }
}
