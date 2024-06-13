package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {


    private int courseId;

    private String courseName;

    private int credit;

    private SemesterForGeneralCoursesDto semester;

    public TeacherDto teacher;


}
