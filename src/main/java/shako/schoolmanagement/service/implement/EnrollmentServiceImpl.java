package shako.schoolmanagement.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.dto.AddEnrollmentDto;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.EnrollmentMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.exception.EnrollmentOutOfLimitException;
import shako.schoolmanagement.exception.EnrollmentOutOfTimeException;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentMapper enrollmentMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<EnrollmentDto> getAllEnrollments() {
        log.debug("Fetching all enrollments");
        List<Enrollment> enrollments = enrollmentRepository.getAllEnrollments();
        log.debug("Found {} enrollments", enrollments.size());
        List<EnrollmentDto> enrollmentDtos = enrollments.stream()
                .map(enrollment -> enrollmentMapper.enrollmentToEnrollmentDto(enrollment)).collect(Collectors.toList());
        for (int i = 0; i < enrollments.size(); i++) {
            enrollmentDtos.get(i).setStudent(studentMapper.studentToStudentDtoForTraining(enrollments.get(i).getStudent()));
            enrollmentDtos.get(i).setCourse(courseMapper.courseToCourseDto(enrollments.get(i).getCourse()));
        }
        return enrollmentDtos;
    }

    @Override
    public void addEnrollment(AddEnrollmentDto enrollmentDto) {
        log.info("Adding enrollment for student: {}, courseId: {}", enrollmentDto.getNeptunCode(), enrollmentDto.getCourseId());
        Optional<Student> loggedInStudent = studentRepository.findByNeptunCode(enrollmentDto.getNeptunCode());
        Student student = loggedInStudent.orElseThrow(() -> {
            log.warn("Student not found for neptunCode: {}", enrollmentDto.getNeptunCode());
            return new StudentNotExistsException("Logged-in student not found");
        });

        Enrollment enrollmentOfStudent = new Enrollment();
        enrollmentOfStudent.setStudent(student);
        Course course = courseRepository.getCourseByCourseId(enrollmentDto.getCourseId());
        enrollmentOfStudent.setCourse(course);
        enrollmentOfStudent.setDateOfRegister(LocalDateTime.now());

        int studentId = student.getUserId();
        List<Enrollment> enrollmentList = enrollmentRepository.getEnrollmentsOfStudentByCourseId(enrollmentDto.getCourseId(), studentId);

        if (!enrollmentList.isEmpty()) {
            if (enrollmentList.size() >= 3) {
                log.warn("Student {} reached enrollment limit for courseId {}", enrollmentDto.getNeptunCode(), enrollmentDto.getCourseId());
                throw new EnrollmentOutOfLimitException("Student has reached the limit of enrollment");
            }
            for (Enrollment a : enrollmentList) {
                if (!a.getDateOfRegister().plusMonths(3).isBefore(LocalDate.now().atStartOfDay())) {
                    log.warn("Student {} re-enrolled course {} within 3 months", enrollmentDto.getNeptunCode(), enrollmentDto.getCourseId());
                    throw new EnrollmentOutOfTimeException("Student has enrolled the course during the last 3 months");
                }
            }
        }

        enrollmentOfStudent.setRegistered(true);
        enrollmentRepository.save(enrollmentOfStudent);
        log.info("Enrollment saved for student: {}, courseId: {}", enrollmentDto.getNeptunCode(), enrollmentDto.getCourseId());
    }

    @Override
    public void deleteEnrollment(Integer enrollmentId) {
        log.info("Deleting enrollment with id: {}", enrollmentId);
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        enrollmentRepository.delete(enrollment);
        log.info("Enrollment {} deleted", enrollmentId);
    }

    @Override
    public List<EnrollmentDto> getMyEnrollments(String neptunCode) {
        log.debug("Fetching enrollments for student: {}", neptunCode);
        List<Enrollment> enrollments = enrollmentRepository.findMyEnrollments(neptunCode);
        log.debug("Found {} active enrollments for student: {}", enrollments.size(), neptunCode);
        return toEnrollmentDtos(enrollments);
    }

    @Override
    public boolean isEnrolled(String neptunCode, int courseId) {
        boolean enrolled = enrollmentRepository.countActiveEnrollment(neptunCode, courseId) > 0;
        log.debug("isEnrolled check — student: {}, courseId: {}, result: {}", neptunCode, courseId, enrolled);
        return enrolled;
    }

    @Override
    public void unregisterFromCourse(String neptunCode, int courseId) {
        log.info("Unregistering student: {} from courseId: {}", neptunCode, courseId);
        enrollmentRepository.deleteByStudentNeptunCodeAndCourseId(neptunCode, courseId);
        log.info("Student {} unregistered from courseId {}", neptunCode, courseId);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByStudentNeptunCode(String neptunCode) {
        log.debug("Fetching all enrollments for student: {}", neptunCode);
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentNeptunCode(neptunCode);
        return toEnrollmentDtos(enrollments);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByCourseId(int courseId) {
        log.debug("Fetching enrollments for courseId: {}", courseId);
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByCourseId(courseId);
        log.debug("Found {} enrollments for courseId: {}", enrollments.size(), courseId);
        return toEnrollmentDtos(enrollments);
    }

    @Override
    public void updateGrade(int enrollmentId, int grade) {
        log.info("Updating grade for enrollmentId: {} to {}", enrollmentId, grade);
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        if (enrollment == null) {
            log.error("Enrollment not found: {}", enrollmentId);
            throw new RuntimeException("Enrollment not found: " + enrollmentId);
        }
        enrollment.setGrade(grade);
        enrollmentRepository.save(enrollment);
        log.info("Grade updated for enrollmentId: {}", enrollmentId);
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
