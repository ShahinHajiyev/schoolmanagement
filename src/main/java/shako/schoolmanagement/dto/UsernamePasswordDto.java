package shako.schoolmanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsernamePasswordDto {

    private String username;
    private String password;
}
