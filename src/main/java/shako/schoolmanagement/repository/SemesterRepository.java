package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.Semester;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {

    @Query(value = "select s.id, s.name, c.course_id, c.course_name from semester s left join course c on s.id = c.semester_id order by s.id", nativeQuery = true)
    List<Semester> getAllSemesters();

}



