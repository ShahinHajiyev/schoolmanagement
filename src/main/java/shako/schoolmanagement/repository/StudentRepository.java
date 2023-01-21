package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

     @Query(value = "select *, u.country from student s join user u on s.id = u.id  where s.id=?", nativeQuery = true)
     Optional<Student> findByUserId(Integer id);


}
