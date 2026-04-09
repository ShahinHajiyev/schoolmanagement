package shako.schoolmanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AddEnrollmentDto {

    @NotNull
    @Positive
    private Integer courseId;
    private String neptunCode;
}
