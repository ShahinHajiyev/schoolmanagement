package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpaDto {
    private String neptunCode;
    private double gpa;
    private int gradedCourses;
    private int totalCredits;
}
