package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query(value = "select * from course c right join enrollment e on c.course_id = e.course_id", nativeQuery = true)
    List<Course> getCourses();

    @Query(value = "select * from course where semester_id in (select s.id from semester s join student st on \n" +
            "s.id = st.semester_id join user u on st.id = u.id and u.neptun_code = :username)", nativeQuery = true )
    List<Course> getAvailableCourses(String username);

    @Query(value = "select * from course where course_id = :courseId", nativeQuery = true )
    Course getCourseByCourseId(int courseId);

}
