package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.CourseDto;
import shako.schoolmanagement.entity.Course;

import java.util.List;

public interface CourseService {

    List<CourseDto> getCourses();

    List<CourseDto> getAvailableCourses();

    CourseDto getCourseByCourseId(int courseId);


}
