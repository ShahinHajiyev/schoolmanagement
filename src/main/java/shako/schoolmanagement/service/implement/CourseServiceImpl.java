package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.service.inter.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
