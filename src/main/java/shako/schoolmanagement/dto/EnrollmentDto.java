package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import shako.schoolmanagement.entity.Student;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EnrollmentDto {

    private int enrollmentId;

    private StudentDto student;

    private CourseDto course;

    private Date dateOfRegister;

    private int grade;
}
