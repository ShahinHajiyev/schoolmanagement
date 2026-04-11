package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    private int trainingId;
    private int numberOfTerms;
    private String module;
    private String status;
    private String organizationName;
    private String languageName;
    private String programName;
    private String studentNeptunCode;
}
