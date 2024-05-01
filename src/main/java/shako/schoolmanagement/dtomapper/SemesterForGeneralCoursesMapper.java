package shako.schoolmanagement.dtomapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import shako.schoolmanagement.dto.SemesterForGeneralCoursesDto;
import shako.schoolmanagement.entity.Semester;

@Configuration
public class SemesterForGeneralCoursesMapper {

    @Autowired
    private ModelMapper mapper;

    public SemesterForGeneralCoursesDto mapSemesterForGeneralCoursesDto(Semester semester){
        return mapper.map(semester, SemesterForGeneralCoursesDto.class);
    }

    public Semester mapSemesterToDto(SemesterForGeneralCoursesDto semesterForGeneralCoursesDto){
        return mapper.map(semesterForGeneralCoursesDto, Semester.class);
    }

}
