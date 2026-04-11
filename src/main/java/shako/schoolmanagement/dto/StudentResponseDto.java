package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Safe outbound representation of a Student — no password, no activationCode, no SSN.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {
    private int userId;
    private String firstName;
    private String lastName;
    private String neptunCode;
    private String email;
    private String country;
    private int rollNumber;
    private LocalDateTime graduationYear;
    private List<String> roles;
}
