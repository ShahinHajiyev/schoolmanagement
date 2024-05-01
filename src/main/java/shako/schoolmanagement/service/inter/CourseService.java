package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.CourseDto;

import java.util.List;

public interface CourseService {

    List<CourseDto> getCourses();

    List<CourseDto> getAvailableCourses();


}
