package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.AddCourseDto;
import shako.schoolmanagement.dto.CourseDetailsDto;
import shako.schoolmanagement.dto.CourseDto;
import shako.schoolmanagement.dto.CourseScheduleDto;
import shako.schoolmanagement.dto.StudentDto;

import java.util.List;

public interface CourseService {

    List<CourseDto> getCourses();

    List<CourseDto> getAvailableCourses();

    CourseDto getCourseByCourseId(int courseId);

    List<StudentDto> getStudentsByCourseId(int courseId);

    void addCourse(AddCourseDto addCourseDto);

    void deleteCourse(int courseId);

    // CourseDetails CRUD
    CourseDetailsDto getCourseDetails(int courseId);

    CourseDetailsDto saveCourseDetails(int courseId, CourseDetailsDto dto);

    // CourseSchedule CRUD
    List<CourseScheduleDto> getScheduleForCourse(int courseId);

    CourseScheduleDto addScheduleEntry(int courseId, CourseScheduleDto dto);

    void deleteScheduleEntry(int scheduleId);
}
