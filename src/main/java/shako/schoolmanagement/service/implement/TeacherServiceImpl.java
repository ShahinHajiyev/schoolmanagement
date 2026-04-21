package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.dto.CourseDto;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dto.StudentDto;
import shako.schoolmanagement.dto.TeacherDto;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.EnrollmentMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.Teacher;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.repository.TeacherRepository;
import shako.schoolmanagement.service.inter.TeacherService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public void addTeacher(TeacherDto dto) {
        log.info("Adding teacher: {} {}", dto.getFirstName(), dto.getLastName());
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacherRepository.save(teacher);
        log.info("Teacher {} {} saved", dto.getFirstName(), dto.getLastName());
    }

    @Override
    public List<TeacherDto> getAllTeachers() {
        log.debug("Fetching all teachers");
        return teacherRepository.findAll().stream()
                .map(t -> new TeacherDto(t.getUserId(), t.getFirstName(), t.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getMyCourses(String neptunCode) {
        log.debug("Fetching courses for teacher: {}", neptunCode);
        List<Course> courses = courseRepository.findByTeacher_NeptunCode(neptunCode);
        log.debug("Found {} courses for teacher: {}", courses.size(), neptunCode);
        return courses.stream().map(courseMapper::courseToCourseDto).collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> getMyStudents(String neptunCode) {
        log.debug("Fetching students for teacher: {}", neptunCode);
        List<Course> courses = courseRepository.findByTeacher_NeptunCode(neptunCode);
        return courses.stream()
                .flatMap(c -> enrollmentRepository.findStudentsByCourseId(c.getCourseId()).stream())
                .distinct()
                .map(studentMapper::studentToStudentDtoForTraining)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getCourseEnrollments(String teacherNeptunCode, int courseId) {
        log.debug("Fetching enrollments for courseId:{} teacher:{}", courseId, teacherNeptunCode);
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByCourseId(courseId);
        return enrollments.stream().map(e -> {
            EnrollmentDto dto = enrollmentMapper.enrollmentToEnrollmentDto(e);
            dto.setStudent(studentMapper.studentToStudentDtoForTraining(e.getStudent()));
            dto.setCourse(courseMapper.courseToCourseDto(e.getCourse()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isTeacherOfEnrollment(String neptunCode, int enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        if (enrollment == null) return false;
        Teacher teacher = enrollment.getCourse().getTeacher();
        boolean result = teacher != null && neptunCode.equals(teacher.getNeptunCode());
        log.debug("isTeacherOfEnrollment — teacher:{}, enrollmentId:{}, result:{}", neptunCode, enrollmentId, result);
        return result;
    }
}
