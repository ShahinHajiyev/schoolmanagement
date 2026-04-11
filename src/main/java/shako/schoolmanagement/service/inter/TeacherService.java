package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.CourseDto;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dto.StudentDto;
import shako.schoolmanagement.dto.TeacherDto;

import java.util.List;

public interface TeacherService {

    void addTeacher(TeacherDto teacherDto);

    List<TeacherDto> getAllTeachers();

    List<CourseDto> getMyCourses(String neptunCode);

    List<StudentDto> getMyStudents(String neptunCode);

    List<EnrollmentDto> getCourseEnrollments(String teacherNeptunCode, int courseId);

    boolean isTeacherOfEnrollment(String neptunCode, int enrollmentId);
}
