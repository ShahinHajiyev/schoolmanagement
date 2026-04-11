package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query(value = "SELECT DISTINCT c.* FROM course c " +
                   "RIGHT JOIN enrollment e ON c.course_id = e.course_id " +
                   "WHERE c.deleted_at IS NULL", nativeQuery = true)
    List<Course> getCourses();

    @Query(value = "SELECT * FROM course WHERE semester_id IN " +
                   "(SELECT s.id FROM semester s JOIN student st ON s.id = st.semester_id " +
                   " JOIN user u ON st.id = u.id AND u.neptun_code = :username) " +
                   "AND deleted_at IS NULL", nativeQuery = true)
    List<Course> getAvailableCourses(String username);

    @Query(value = "SELECT * FROM course WHERE course_id = :courseId AND deleted_at IS NULL", nativeQuery = true)
    Course getCourseByCourseId(int courseId);

    @Query("SELECT COUNT(c) FROM Course c WHERE c.semester.id = (SELECT MAX(s.id) FROM Semester s)")
    long countCoursesLatestSemester();

    Optional<Course> findByCourseId(int courseId);

    List<Course> findByTeacher_NeptunCode(String neptunCode);
}
