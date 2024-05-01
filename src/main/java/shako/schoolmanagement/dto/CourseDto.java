package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.entity.Semester;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {


    private int courseId;

    private String courseName;

    private int credit;

    private SemesterForGeneralCoursesDto semester;


}
