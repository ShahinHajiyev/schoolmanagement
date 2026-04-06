package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.Enrollment;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    @Query(value = "Select * from enrollment e where e.student_id=10", nativeQuery = true)
    @Override
    List<Enrollment> findAll();

    @Query(value = "Select * from enrollment", nativeQuery = true)
    List<Enrollment> getAllEnrollments();

    Enrollment findByEnrollmentId(Integer enrollmentId);

    @Query(value = "Select * from enrollment e where e.course_id = :courseId and student_id = :studentId", nativeQuery = true)
    List<Enrollment> getEnrollmentsOfStudentByCourseId(Integer courseId, Integer studentId);

    @Query(value = "Select distinct(e.course_id) from enrollment e where e.is_registered is not null or e.is_finished is not null" +
            " and e.student_id = :studentId", nativeQuery = true)
    List<Integer> getActiveEnrollmentsOfStudent(Integer studentId);
}
