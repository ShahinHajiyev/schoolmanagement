package shako.schoolmanagement.dtomapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dto.UsernamePasswordDto;
import shako.schoolmanagement.entity.Student;


@Configuration
public class StudentUserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public StudentUserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Student dtoToStudentEntity(StudentUserDto studentUserDto){

        return modelMapper.map(studentUserDto, Student.class);
    }

}
