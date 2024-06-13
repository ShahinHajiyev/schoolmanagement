package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.CourseDto;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.SemesterForGeneralCoursesMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.service.inter.CourseService;

import java.util.List;
import java.util.stream.Collectors;

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


    private Authentication authentication;

    @Override
    public List<CourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();

/*        List<Student> students = courses.stream()
                .map(Course::getEnrollments)
                .flatMap(enrollmentList->enrollmentList.stream()
                        .map(Enrollment::getStudent)).toList();

        List<StudentDto> studentDto = students.stream()
                .map(studentMapper::studentToStudentDtoForTraining).toList();

        return courses;*/

        List<CourseDto> courseDtos= courses.stream().map(courseMapper::courseToCourseDto).toList();

        for (int i = 0; i < courses.size(); i++) {
            courseDtos.get(i).setSemester(semesterForGeneralCoursesMapper.mapSemesterForGeneralCoursesDto(courses.get(i).getSemester()));
        }

        return courseDtos;
    }

    @Override
    public List<CourseDto> getAvailableCourses() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Course> availableCourses = courseRepository.getAvailableCourses(username);
        List<CourseDto> courseDtos= availableCourses.stream().map(courseMapper::courseToCourseDto).toList();


        return courseDtos;
    }

    @Override
    public CourseDto getCourseByCourseId(int courseId) {
        //Course courseFromDB = courseRepository.getCourseByCourseId(courseId);
        Course courseFromDB = courseRepository.getCourseByCourseId(courseId);
        CourseDto courseDto = courseMapper.courseToCourseDto(courseFromDB);
        return courseDto;
    }


}

