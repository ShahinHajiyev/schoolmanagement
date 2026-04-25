package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.ScheduleEntryDto;
import shako.schoolmanagement.service.inter.ScheduleService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public List<ScheduleEntryDto> getSchedule(@RequestParam(required = false) String weekStart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String neptunCode = authentication.getName();
        log.info("GET /schedule — student: {}, weekStart: {}", neptunCode, weekStart);
        return scheduleService.getScheduleForStudent(neptunCode, weekStart);
    }
}
