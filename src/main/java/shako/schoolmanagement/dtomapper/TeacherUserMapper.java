package shako.schoolmanagement.dtomapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.entity.Teacher;

@Configuration
public class TeacherUserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TeacherUserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Teacher dtoToTeacherEntity(StudentUserDto studentUserDto) {
        return modelMapper.map(studentUserDto, Teacher.class);
    }
}
