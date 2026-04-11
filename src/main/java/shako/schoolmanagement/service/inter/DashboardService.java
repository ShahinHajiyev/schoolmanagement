package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.DashboardStatsDto;
import shako.schoolmanagement.dto.RecentEnrollmentDto;

import java.util.List;

public interface DashboardService {

    DashboardStatsDto getStats();

    List<RecentEnrollmentDto> getRecentEnrollments();
}
