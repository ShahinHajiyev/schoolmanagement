package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private String userName;

    @NotNull
    private String password;

    @NotNull
    private String email;

    private LocalDateTime created;
}
