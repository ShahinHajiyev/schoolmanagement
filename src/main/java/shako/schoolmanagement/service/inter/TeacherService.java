package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.TeacherDto;
import shako.schoolmanagement.entity.Teacher;

import java.util.List;

public interface TeacherService {

    void addTeacher(TeacherDto teacherDto);

    List<Teacher> getAllTeachers();
}
