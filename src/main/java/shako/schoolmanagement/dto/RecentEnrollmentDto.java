package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentEnrollmentDto {
    private String studentName;
    private String courseName;
    private LocalDateTime enrolledAt;
}
