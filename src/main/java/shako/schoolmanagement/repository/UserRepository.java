package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.entity.Student;
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

    @Query(value = "select *, r.role_name, 0 AS clazz_ " +
            "from user u join user_role ur " +
            "on u.id = ur.user_id join role r " +
            "on ur.role_id = r.role_id " +
            "where u.neptun_code = ?", nativeQuery = true)
    Optional<User> findByNeptunCode(String neptunCode);

    @Query(value = "select *, r.role_name, 0 AS clazz_ " +
            "from user u join user_role ur " +
            "on u.id = ur.user_id join role r " +
            "on ur.role_id = r.role_id " +
            "where u.neptun_code = ?", nativeQuery = true)
    User findByNeptun(String neptunCode);


    @Query(value = "SELECT u FROM User u WHERE u.email = :email")
    Student findStudentByEmail(String email);

    Optional<User> findByEmail(String mail);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER u SET u.activation_code = :activationCode where u.neptun_code = :neptunCode", nativeQuery = true)
    void saveActivationCode(String neptunCode, String activationCode);


}
