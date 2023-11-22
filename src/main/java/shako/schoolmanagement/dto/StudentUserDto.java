package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StudentUserDto {

/*    @NotNull
    private int userId;*/

    @NotNull
    private String username;

    @NotNull
    private String password;

/*    @NotNull
    @Email(message = "Please enter the correct email")
    private String email;*/

    private LocalDateTime created;
}
