package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.dto.ScheduleEntryDto;
import shako.schoolmanagement.entity.CourseSchedule;
import shako.schoolmanagement.entity.Semester;
import shako.schoolmanagement.repository.CourseScheduleRepository;
import shako.schoolmanagement.service.inter.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class  ScheduleServiceImpl implements ScheduleService {

    private final CourseScheduleRepository courseScheduleRepository;

    @Override
    public List<ScheduleEntryDto> getScheduleForStudent(String neptunCode, String weekStart) {
        LocalDate weekStartDate = weekStart != null
                ? LocalDate.parse(weekStart)
                : LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate weekEndDate = weekStartDate.plusDays(6);
        log.info("Fetching schedule for student: {}, week: {} to {}", neptunCode, weekStartDate, weekEndDate);

        List<CourseSchedule> entries = courseScheduleRepository.findScheduleForStudent(neptunCode);
        log.debug("Total schedule entries before semester filter: {}", entries.size());

        List<ScheduleEntryDto> result = entries.stream()
                .filter(cs -> {
                    boolean inRange = isInSemesterRange(cs.getCourse().getSemester(), weekStartDate, weekEndDate);
                    if (!inRange) {
                        log.debug("Filtered out course '{}' (semester: {}) — not active in week {} to {}",
                                cs.getCourse().getCourseName(),
                                cs.getCourse().getSemester() != null ? cs.getCourse().getSemester().getName() : "none",
                                weekStartDate, weekEndDate);
                    }
                    return inRange;
                })
                .map(cs -> {
                    String teacherName = cs.getCourse().getTeacher() != null
                            ? cs.getCourse().getTeacher().getFirstName() + " " + cs.getCourse().getTeacher().getLastName()
                            : null;
                    int dayNum = dayNameToNumber(cs.getDayOfWeek());
                    String classDate = weekStartDate.plusDays(dayNum - 1).toString();
                    log.debug("Mapped entry — course: '{}', day: {}, date: {}, time: {}-{}",
                            cs.getCourse().getCourseName(), cs.getDayOfWeek(), classDate,
                            cs.getStartTime(), cs.getEndTime());
                    return new ScheduleEntryDto(dayNum, cs.getStartTime(), cs.getEndTime(),
                            cs.getCourse().getCourseName(), teacherName, cs.getRoom(), classDate);
                }).collect(Collectors.toList());

        log.info("Returning {} schedule entries for student: {}", result.size(), neptunCode);
        return result;
    }

    private boolean isInSemesterRange(Semester semester, LocalDate weekStart, LocalDate weekEnd) {
        if (semester == null) {
            log.debug("No semester on course — treating as always active");
            return true;
        }
        int semNum = semesterNameToNumber(semester.getName());
        int year = LocalDate.now().getYear();
        LocalDate semStart, semEnd;
        if (semNum % 2 == 1) {
            semStart = LocalDate.of(year, 9, 1);
            semEnd   = LocalDate.of(year, 12, 31);
        } else {
            semStart = LocalDate.of(year, 2, 1);
            semEnd   = LocalDate.of(year, 5, 31);
        }
        log.debug("Semester '{}' (#{}) range for year {}: {} to {}", semester.getName(), semNum, year, semStart, semEnd);
        return !weekEnd.isBefore(semStart) && !weekStart.isAfter(semEnd);
    }

    private int semesterNameToNumber(String name) {
        if (name == null) {
            log.warn("Semester name is null, defaulting to semester 1 (odd/autumn range)");
            return 1;
        }
        return switch (name.trim().toLowerCase()) {
            case "first"   -> 1;
            case "second"  -> 2;
            case "third"   -> 3;
            case "fourth"  -> 4;
            case "fifth"   -> 5;
            case "sixth"   -> 6;
            case "seventh" -> 7;
            case "eighth"  -> 8;
            default        -> {
                log.warn("Unknown semester name '{}', defaulting to semester 1 (odd/autumn range)", name);
                yield 1;
            }
        };
    }

    private int dayNameToNumber(String day) {
        if (day == null) return 0;
        return switch (day.trim().toLowerCase()) {
            case "monday"    -> 1;
            case "tuesday"   -> 2;
            case "wednesday" -> 3;
            case "thursday"  -> 4;
            case "friday"    -> 5;
            case "saturday"  -> 6;
            default          -> 0;
        };
    }
}
