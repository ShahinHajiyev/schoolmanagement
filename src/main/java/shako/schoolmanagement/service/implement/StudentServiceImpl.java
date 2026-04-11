package shako.schoolmanagement.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.*;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.EnrollmentMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.service.inter.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              EnrollmentRepository enrollmentRepository,
                              EnrollmentMapper enrollmentMapper,
                              StudentMapper studentMapper,
                              CourseMapper courseMapper) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public Page<StudentResponseDto> getAll(Pageable pageable) {
        log.debug("Fetching students page: {}", pageable);
        return studentRepository.findAll(pageable).map(this::toResponseDto);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByNeptunCode(String neptunCode) {
        log.debug("Fetching enrollments for student: {}", neptunCode);
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentNeptunCode(neptunCode);
        return toEnrollmentDtos(enrollments);
    }

    @Override
    public GpaDto getGpa(String neptunCode) {
        log.debug("Calculating GPA for student: {}", neptunCode);
        List<Enrollment> finished = enrollmentRepository.findEnrollmentsByStudentNeptunCode(neptunCode)
                .stream()
                .filter(e -> e.isFinished() && e.getGrade() > 0)
                .collect(Collectors.toList());

        if (finished.isEmpty()) {
            return new GpaDto(neptunCode, 0.0, 0, 0);
        }

        double totalWeightedGrade = finished.stream()
                .mapToDouble(e -> (double) e.getGrade() * e.getCourse().getCredit())
                .sum();
        int totalCredits = finished.stream()
                .mapToInt(e -> e.getCourse().getCredit())
                .sum();
        double gpa = totalCredits > 0 ? totalWeightedGrade / totalCredits : 0.0;

        log.debug("GPA for {}: {} over {} credits", neptunCode, gpa, totalCredits);
        return new GpaDto(neptunCode, Math.round(gpa * 100.0) / 100.0, finished.size(), totalCredits);
    }

    @Override
    public List<TranscriptItemDto> getTranscript(String neptunCode) {
        log.debug("Fetching transcript for student: {}", neptunCode);
        studentRepository.findByNeptunCode(neptunCode)
                .orElseThrow(() -> new StudentNotExistsException("Student not found: " + neptunCode));

        return enrollmentRepository.findEnrollmentsByStudentNeptunCode(neptunCode)
                .stream()
                .map(e -> new TranscriptItemDto(
                        e.getCourse().getCourseName(),
                        e.getCourse().getCredit(),
                        e.getGrade(),
                        e.getCourse().getSemester() != null ? e.getCourse().getSemester().getName() : null,
                        e.isFinished()))
                .collect(Collectors.toList());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private StudentResponseDto toResponseDto(Student s) {
        List<String> roles = s.getRoles().stream()
                .map(r -> r.getRoleName())
                .collect(Collectors.toList());
        return new StudentResponseDto(
                s.getUserId(), s.getFirstName(), s.getLastName(),
                s.getNeptunCode(), s.getEmail(), s.getCountry(),
                s.getRollNumber(), s.getGraduationYear(), roles);
    }

    private List<EnrollmentDto> toEnrollmentDtos(List<Enrollment> enrollments) {
        List<EnrollmentDto> dtos = enrollments.stream()
                .map(enrollmentMapper::enrollmentToEnrollmentDto)
                .collect(Collectors.toList());
        for (int i = 0; i < enrollments.size(); i++) {
            dtos.get(i).setStudent(studentMapper.studentToStudentDtoForTraining(enrollments.get(i).getStudent()));
            dtos.get(i).setCourse(courseMapper.courseToCourseDto(enrollments.get(i).getCourse()));
        }
        return dtos;
    }
}
