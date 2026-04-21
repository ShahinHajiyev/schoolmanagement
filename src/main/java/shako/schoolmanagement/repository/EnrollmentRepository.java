package shako.schoolmanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.entity.Student;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course")
    List<Enrollment> getAllEnrollments();

    Enrollment findByEnrollmentId(Integer enrollmentId);

    @Query(value = "Select * from enrollment e where e.course_id = :courseId and student_id = :studentId", nativeQuery = true)
    List<Enrollment> getEnrollmentsOfStudentByCourseId(Integer courseId, Integer studentId);

    @Query(value = "Select distinct(e.course_id) from enrollment e where e.is_registered is not null or e.is_finished is not null" +
            " and e.student_id = :studentId", nativeQuery = true)
    List<Integer> getActiveEnrollmentsOfStudent(Integer studentId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.neptunCode = :neptunCode AND e.isRegistered = true")
    List<Enrollment> findMyEnrollments(String neptunCode);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.neptunCode = :neptunCode AND e.course.courseId = :courseId AND e.isRegistered = true")
    long countActiveEnrollment(String neptunCode, int courseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Enrollment e WHERE e.student.neptunCode = :neptunCode AND e.course.courseId = :courseId")
    void deleteByStudentNeptunCodeAndCourseId(String neptunCode, int courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.neptunCode = :neptunCode")
    List<Enrollment> findEnrollmentsByStudentNeptunCode(String neptunCode);

    @Query("SELECT e FROM Enrollment e WHERE e.course.courseId = :courseId")
    List<Enrollment> findEnrollmentsByCourseId(int courseId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.isRegistered = true")
    long countActiveEnrollments();

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course ORDER BY e.dateOfRegister DESC")
    List<Enrollment> findRecentEnrollments(Pageable pageable);

    @Query("SELECT DISTINCT e.student FROM Enrollment e WHERE e.course.courseId = :courseId AND e.isRegistered = true")
    List<Student> findStudentsByCourseId(int courseId);
}
