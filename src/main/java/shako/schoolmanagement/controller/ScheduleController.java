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
import shako.schoolmanagement.entity.CourseSchedule;
import shako.schoolmanagement.repository.CourseScheduleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final CourseScheduleRepository courseScheduleRepository;

    @GetMapping
    public List<ScheduleEntryDto> getSchedule(@RequestParam(required = false) String weekStart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String neptunCode = authentication.getName();
        log.info("GET /schedule — student: {}, weekStart: {}", neptunCode, weekStart);

        List<CourseSchedule> schedule = courseScheduleRepository.findScheduleForStudent(neptunCode);
        return schedule.stream().map(cs -> {
            String teacherName = cs.getCourse().getTeacher() != null
                    ? cs.getCourse().getTeacher().getFirstName() + " " + cs.getCourse().getTeacher().getLastName()
                    : null;
            return new ScheduleEntryDto(
                    cs.getDayOfWeek(), cs.getStartTime(), cs.getEndTime(),
                    cs.getCourse().getCourseName(), teacherName, cs.getRoom());
        }).collect(Collectors.toList());
    }
}
