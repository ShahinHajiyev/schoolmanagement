package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.CourseDto;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.SemesterForGeneralCoursesMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.exception.StudentNotExistsException;
import shako.schoolmanagement.service.inter.CourseService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private SemesterForGeneralCoursesMapper semesterForGeneralCoursesMapper;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;


    @Override
    public List<CourseDto> getCourses() {

        List<Course> courses = courseRepository.findAll();

        List<CourseDto> courseDtos= courses.stream().map(courseMapper::courseToCourseDto).toList();

        for (int i = 0; i < courses.size(); i++) {
            courseDtos.get(i).setSemester(semesterForGeneralCoursesMapper.mapSemesterForGeneralCoursesDto(courses.get(i).getSemester()));
        }

        return courseDtos;
    }

    @Override
    public List<CourseDto> getAvailableCourses() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String neptunCode = authentication.getName();
        Optional<Student> loggedInStudent = studentRepository.findByNeptunCode(neptunCode);
        int studentId = loggedInStudent.orElseThrow(() -> new StudentNotExistsException("Logged-in student not found")).getUserId();

        List<Course> availableCoursesOfStudent = courseRepository.getAvailableCourses(neptunCode);
        List<Integer> activeEnrollmentsOfStudent = enrollmentRepository.getActiveEnrollmentsOfStudent(studentId);
        List<Course> availableCoursesOfStudentAfterEnrollment =  availableCoursesOfStudent.stream().filter(a -> !activeEnrollmentsOfStudent.contains(a.getCourseId())).toList();
        List<CourseDto> availableCoursesOfStudentAfterEnrollmentDtoList = availableCoursesOfStudentAfterEnrollment.stream().map(a -> courseMapper.courseToCourseDto(a)).toList();

        return availableCoursesOfStudentAfterEnrollmentDtoList;
    }

    @Override
    public CourseDto getCourseByCourseId(int courseId) {
        Course courseFromDB = courseRepository.getCourseByCourseId(courseId);
        CourseDto courseDto = courseMapper.courseToCourseDto(courseFromDB);
        return courseDto;
    }


}

