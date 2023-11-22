package shako.schoolmanagement.dtomapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.entity.Student;

@Configuration
public class AdminStudentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Student dtoToStudentEntity(AdminStudentDto adminStudentDto){
        return modelMapper.map(adminStudentDto, Student.class);
    }
}
