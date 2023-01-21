package shako.schoolmanagement.dtomapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.entity.Enrollment;

@Configuration
public class EnrollmentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public EnrollmentDto enrollmentToEnrollmentDto(Enrollment enrollment){
        return modelMapper.map(enrollment, EnrollmentDto.class);
    }
}
