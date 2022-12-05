package shako.schoolmanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {

    @NotNull
    private String userName;

    @NotNull
    private String password;

    @NotNull
    @Email(message = "Please enter the right email")
    private String email;

    private Timestamp created;

}
