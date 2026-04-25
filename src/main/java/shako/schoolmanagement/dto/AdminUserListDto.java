package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserListDto {
    private String neptunCode;
    private String email;
    private String role;
    private int activationBlockPhase;
}
