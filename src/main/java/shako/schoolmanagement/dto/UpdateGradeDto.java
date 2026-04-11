package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGradeDto {

    @Min(value = 1, message = "Grade must be at least 1")
    @Max(value = 5, message = "Grade must be at most 5")
    private int grade;
}
