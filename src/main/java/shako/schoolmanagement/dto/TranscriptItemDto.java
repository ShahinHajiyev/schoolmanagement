package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptItemDto {
    private String courseName;
    private int credit;
    private int grade;
    private String semesterName;
    private boolean finished;
}
