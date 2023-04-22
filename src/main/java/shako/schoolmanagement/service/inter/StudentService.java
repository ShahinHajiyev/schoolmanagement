package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.entity.Student;

import java.util.List;

public interface StudentService {

    void addStudent(StudentUserDto studentUserDto);

    List<Student> getAll();
}
