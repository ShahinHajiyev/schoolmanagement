package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.ScheduleEntryDto;

import java.util.List;

public interface ScheduleService {

    List<ScheduleEntryDto> getScheduleForStudent(String neptunCode, String weekStart);
}
