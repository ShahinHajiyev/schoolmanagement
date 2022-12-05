package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.entity.Student;

public interface StudentService {

    void addStudent(StudentUserDto studentUserDto);

}
