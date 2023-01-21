package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.StudentDto;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.service.inter.CourseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final StudentMapper studentMapper;

    @Override
    public List<Course> getCourses() {
        List<Course> courses = courseRepository.findAll();

        List<Student> students = courses.stream()
                .map(Course::getEnrollments)
                .flatMap(enrollmentList->enrollmentList.stream()
                        .map(Enrollment::getStudent)).collect(Collectors.toList());

        List<StudentDto> studentDto = students.stream()
                .map(student -> studentMapper.studentToStudentDtoForTraining(student)).collect(Collectors.toList());

        return courses;
    }
}

