package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCourseDto {

    @NotBlank(message = "Course name must not be blank")
    private String courseName;

    @Positive(message = "Credit must be positive")
    private int credit;

    @NotBlank(message = "Teacher neptun code must not be blank")
    private String teacherNeptunCode;

    @Positive(message = "Semester ID must be positive")
    private int semesterId;
}
