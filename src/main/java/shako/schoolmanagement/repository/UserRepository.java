package shako.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


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
    @Query(value = "UPDATE USER u SET u.activation_code = :activationCode, u.activation_code_expiry = :expiry WHERE u.neptun_code = :neptunCode", nativeQuery = true)
    void saveActivationCode(String neptunCode, String activationCode, LocalDateTime expiry);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET activation_attempts = :attempts, activation_blocked_until = :blockedUntil, activation_block_phase = :phase WHERE neptun_code = :neptunCode", nativeQuery = true)
    void updateActivationLockout(String neptunCode, int attempts, LocalDateTime blockedUntil, int phase);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET is_active = true, activation_attempts = 0, activation_block_phase = 0, activation_blocked_until = NULL WHERE neptun_code = :neptunCode", nativeQuery = true)
    void activateAndClearLockout(String neptunCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET activation_attempts = 0, activation_block_phase = 0, activation_blocked_until = NULL WHERE neptun_code = :neptunCode", nativeQuery = true)
    void clearActivationLockout(String neptunCode);

    Optional<User> findByPasswordResetToken(String token);
}
