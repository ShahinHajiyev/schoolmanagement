package shako.schoolmanagement.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.DashboardStatsDto;
import shako.schoolmanagement.dto.RecentEnrollmentDto;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.service.inter.DashboardService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public DashboardStatsDto getStats() {
        log.debug("Fetching dashboard stats");
        long totalStudents = studentRepository.count();
        long activeEnrollments = enrollmentRepository.countActiveEnrollments();
        long totalCourses = courseRepository.count();
        long coursesThisSemester = courseRepository.countCoursesLatestSemester();
        log.debug("Stats — students: {}, activeEnrollments: {}, totalCourses: {}, thisSemester: {}",
                totalStudents, activeEnrollments, totalCourses, coursesThisSemester);
        return new DashboardStatsDto(totalStudents, activeEnrollments, totalCourses, coursesThisSemester);
    }

    @Override
    public List<RecentEnrollmentDto> getRecentEnrollments() {
        log.debug("Fetching 10 most recent enrollments");
        List<Enrollment> recent = enrollmentRepository.findRecentEnrollments(PageRequest.of(0, 10));
        return recent.stream().map(e -> {
            String studentName = e.getStudent().getFirstName() + " " + e.getStudent().getLastName();
            String courseName = e.getCourse().getCourseName();
            return new RecentEnrollmentDto(studentName, courseName, e.getDateOfRegister());
        }).collect(Collectors.toList());
    }
}
