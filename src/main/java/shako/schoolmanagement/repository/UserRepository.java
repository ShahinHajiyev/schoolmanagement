package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shako.schoolmanagement.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select *, r.role_name, 0 AS clazz_ " +
            "from user u join user_role ur " +
            "on u.id = ur.user_id join role r " +
            "on ur.role_id = r.role_id " +
            "where u.username = ?", nativeQuery = true)
    Optional<User> findByUserName(String username);


    Optional<String> findByEmail(String email);
}
