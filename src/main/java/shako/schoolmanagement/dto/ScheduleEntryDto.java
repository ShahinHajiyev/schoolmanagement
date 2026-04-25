package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntryDto {
    private int dayOfWeek;
    private String startTime;
    private String endTime;
    private String courseName;
    private String teacherName;
    private String room;
    private String date;
}
