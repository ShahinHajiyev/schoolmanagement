package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shako.schoolmanagement.validator.ValidEmail;
import shako.schoolmanagement.validator.ValidNeptunCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "neptun_code")
    @ValidNeptunCode
    private String neptunCode;

    @Column(name = "email")
    @ValidEmail
    private String email;

    @CreatedDate
    @Column(name = "created", updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "education_id")
    private Long educationId;

    @Column(name = "country")
    private String country;

    @JsonIgnore
    @Column(name = "social_security_number")
    private String socialSecurityNumber;

    @JsonIgnore
    @Column(name = "activation_code")
    private String activationCode;

    @JsonIgnore
    @Column(name = "activation_code_expiry")
    private LocalDateTime activationCodeExpiry;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /** Token sent via email for password reset. */
    @JsonIgnore
    @Column(name = "password_reset_token")
    private String passwordResetToken;

    /** Expiry of the password reset token. */
    @JsonIgnore
    @Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenExpiry;

    public User(int userId) {
        this.userId = userId;
    }

    public User(int userId, String password, String firstName, String lastName,
                String neptunCode, String email, LocalDateTime created,
                List<Role> roles, String country) {
        this.userId = userId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.neptunCode = neptunCode;
        this.email = email;
        this.created = created;
        this.roles = roles;
        this.country = country;
    }

    public User(int userId, String neptunCode, String email) {
        this.userId = userId;
        this.neptunCode = neptunCode;
        this.email = email;
    }

    public User() { }

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();
}
