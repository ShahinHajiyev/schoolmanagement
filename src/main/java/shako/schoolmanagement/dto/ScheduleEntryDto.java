package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntryDto {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String courseName;
    private String teacherName;
    private String room;
}
