package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.dto.*;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.SemesterForGeneralCoursesMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.*;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.repository.*;
import shako.schoolmanagement.service.inter.CourseService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired private CourseRepository courseRepository;
    @Autowired private StudentMapper studentMapper;
    @Autowired private CourseMapper courseMapper;
    @Autowired private SemesterForGeneralCoursesMapper semesterForGeneralCoursesMapper;
    @Autowired private StudentRepository studentRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private SemesterRepository semesterRepository;
    @Autowired private CourseScheduleRepository courseScheduleRepository;

    @Override
    public List<CourseDto> getCourses() {
        log.debug("Fetching all courses");
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> dtos = courses.stream().map(courseMapper::courseToCourseDto).toList();
        for (int i = 0; i < courses.size(); i++) {
            dtos.get(i).setSemester(semesterForGeneralCoursesMapper
                    .mapSemesterForGeneralCoursesDto(courses.get(i).getSemester()));
        }
        return dtos;
    }

    @Override
    public List<CourseDto> getAvailableCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String neptunCode = auth.getName();
        Student student = studentRepository.findByNeptunCode(neptunCode)
                .orElseThrow(() -> new StudentNotExistsException("Logged-in student not found"));
        List<Course> available = courseRepository.getAvailableCourses(neptunCode);
        List<Integer> active = enrollmentRepository.getActiveEnrollmentsOfStudent(student.getUserId());
        return available.stream()
                .filter(c -> !active.contains(c.getCourseId()))
                .map(courseMapper::courseToCourseDto)
                .toList();
    }

    @Override
    public CourseDto getCourseByCourseId(int courseId) {
        return courseMapper.courseToCourseDto(courseRepository.getCourseByCourseId(courseId));
    }

    @Override
    public List<StudentDto> getStudentsByCourseId(int courseId) {
        return enrollmentRepository.findStudentsByCourseId(courseId).stream()
                .map(studentMapper::studentToStudentDtoForTraining).toList();
    }

    @Override
    public void addCourse(AddCourseDto dto) {
        log.info("Adding course '{}' teacher:{} semester:{}", dto.getCourseName(),
                dto.getTeacherNeptunCode(), dto.getSemesterId());
        Teacher teacher = teacherRepository.findByNeptunCode(dto.getTeacherNeptunCode())
                .orElseThrow(() -> new RuntimeException("Teacher not found: " + dto.getTeacherNeptunCode()));
        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found: " + dto.getSemesterId()));
        Course course = new Course();
        course.setCourseName(dto.getCourseName());
        course.setCredit(dto.getCredit());
        course.setTeacher(teacher);
        course.setSemester(semester);
        courseRepository.save(course);
        log.info("Course '{}' saved", dto.getCourseName());
    }

    @Override
    public void deleteCourse(int courseId) {
        log.info("Soft-deleting course id:{}", courseId);
        // @SQLDelete on Course rewrites DELETE → UPDATE deleted_at=NOW()
        courseRepository.deleteById(courseId);
    }

    // ── CourseDetails ─────────────────────────────────────────────────────────

    @Override
    public CourseDetailsDto getCourseDetails(int courseId) {
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));
        CourseDetails cd = course.getCourseDetails();
        if (cd == null) return null;
        return toDetailsDto(cd);
    }

    @Override
    public CourseDetailsDto saveCourseDetails(int courseId, CourseDetailsDto dto) {
        log.info("Saving course details for courseId:{}", courseId);
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));
        CourseDetails cd = course.getCourseDetails() != null ? course.getCourseDetails() : new CourseDetails();
        cd.setCourseCode(dto.getCourseCode());
        cd.setExamType(dto.getExamType());
        cd.setCourseType(dto.getCourseType());
        cd.setClassSchedule(dto.getClassSchedule());
        cd.setInternetAddress(dto.getInternetAddress());
        cd.setCoursePrice(dto.getCoursePrice());
        cd.setMinHeadcount(dto.getMinHeadcount());
        cd.setMaxHeadcount(dto.getMaxHeadcount());
        cd.setPreliminaryRequirement(dto.getPreliminaryRequirement());
        cd.setClassPerWeek(dto.getClassPerWeek());
        cd.setClassPerTerm(dto.getClassPerTerm());
        cd.setCourse(course);
        course.setCourseDetails(cd);
        courseRepository.save(course);
        return toDetailsDto(cd);
    }

    // ── CourseSchedule ────────────────────────────────────────────────────────

    @Override
    public List<CourseScheduleDto> getScheduleForCourse(int courseId) {
        return courseScheduleRepository.findByCourse_CourseId(courseId)
                .stream().map(this::toScheduleDto).toList();
    }

    @Override
    public CourseScheduleDto addScheduleEntry(int courseId, CourseScheduleDto dto) {
        log.info("Adding schedule entry for courseId:{} day:{}", courseId, dto.getDayOfWeek());
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));
        CourseSchedule entry = CourseSchedule.builder()
                .course(course)
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .room(dto.getRoom())
                .build();
        CourseSchedule saved = courseScheduleRepository.save(entry);
        return toScheduleDto(saved);
    }

    @Override
    public void deleteScheduleEntry(int scheduleId) {
        log.info("Deleting schedule entry id:{}", scheduleId);
        courseScheduleRepository.deleteById(scheduleId);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private CourseDetailsDto toDetailsDto(CourseDetails cd) {
        return new CourseDetailsDto(
                cd.getCourseDetailsId(), cd.getCourseCode(), cd.getExamType(), cd.getCourseType(),
                cd.getClassSchedule(), cd.getInternetAddress(), cd.getCoursePrice(),
                cd.getMinHeadcount(), cd.getMaxHeadcount(), cd.getPreliminaryRequirement(),
                cd.getClassPerWeek(), cd.getClassPerTerm(),
                cd.getOrganization() != null ? cd.getOrganization().getOrganizationName() : null,
                cd.getLanguage() != null ? cd.getLanguage().getLanguageName() : null);
    }

    private CourseScheduleDto toScheduleDto(CourseSchedule cs) {
        String teacherName = cs.getCourse().getTeacher() != null
                ? cs.getCourse().getTeacher().getFirstName() + " " + cs.getCourse().getTeacher().getLastName()
                : null;
        return new CourseScheduleDto(cs.getId(), cs.getCourse().getCourseId(),
                cs.getCourse().getCourseName(), teacherName,
                cs.getDayOfWeek(), cs.getStartTime(), cs.getEndTime(), cs.getRoom());
    }
}
