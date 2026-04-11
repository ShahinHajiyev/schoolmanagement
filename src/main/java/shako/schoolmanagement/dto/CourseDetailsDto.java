package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailsDto {
    private int courseDetailsId;

    @NotBlank
    private String courseCode;
    private String examType;
    private String courseType;
    private String classSchedule;
    private String internetAddress;

    @Min(0)
    private int coursePrice;

    @Min(0)
    private int minHeadcount;

    @Min(0)
    private int maxHeadcount;

    private String preliminaryRequirement;
    private int classPerWeek;
    private int classPerTerm;
    private String organizationName;
    private String languageName;
}
