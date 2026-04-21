package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.Program;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
}
