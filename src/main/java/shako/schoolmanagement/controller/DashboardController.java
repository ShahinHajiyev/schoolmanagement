package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.DashboardStatsDto;
import shako.schoolmanagement.dto.RecentEnrollmentDto;
import shako.schoolmanagement.service.inter.DashboardService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardStatsDto getStats() {
        log.info("GET /dashboard/stats");
        return dashboardService.getStats();
    }

    @GetMapping("/recent-enrollments")
    public List<RecentEnrollmentDto> getRecentEnrollments() {
        log.info("GET /dashboard/recent-enrollments");
        return dashboardService.getRecentEnrollments();
    }
}
