package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.Training;

import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {

    Optional<Training> findByStudent_NeptunCode(String neptunCode);
}
