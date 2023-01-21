package shako.schoolmanagement.dtomapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shako.schoolmanagement.dto.StudentDto;
import shako.schoolmanagement.entity.Student;

@Configuration
public class StudentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public StudentDto studentToStudentDtoForTraining(Student student){

      return modelMapper.map(student, StudentDto.class);
    }

    public Student studentDtoToStudentMapper(StudentDto studentDto){

        return modelMapper.map(studentDto, Student.class);
    }
}
