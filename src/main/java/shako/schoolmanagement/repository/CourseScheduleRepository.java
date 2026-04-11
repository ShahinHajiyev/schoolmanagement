package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.CourseSchedule;

import java.util.List;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Integer> {

    List<CourseSchedule> findByCourse_CourseId(int courseId);

    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.course.courseId IN " +
           "(SELECT e.course.courseId FROM Enrollment e WHERE e.student.neptunCode = :neptunCode AND e.isRegistered = true)")
    List<CourseSchedule> findScheduleForStudent(String neptunCode);
}
