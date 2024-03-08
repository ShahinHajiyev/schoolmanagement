package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import shako.schoolmanagement.validator.ValidEmail;
import shako.schoolmanagement.validator.ValidNeptunCode;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdminStudentDto {

    @NotNull
    @ValidNeptunCode(message = "invalid neptun code")
    private String neptunCode;

    @NotNull
    @ValidEmail
    private String email;
}
