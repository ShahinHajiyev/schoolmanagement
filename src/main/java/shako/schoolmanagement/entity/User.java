package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import shako.schoolmanagement.validator.ValidEmail;
import shako.schoolmanagement.validator.ValidNeptunCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "user")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
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

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "education_id")
    private Long educationId;

    @Column(name = "country")
    private String country;

    @Column(name = "social_security_number")
    private int socialSecurityNumber;

    @Column(name = "validation")
    private String validation;

    public User(int userId) {
        this.userId = userId;
    }

    public User(int userId,
                String userName,
                String password,
                String firstName,
                String lastName,
                String neptunCode,
                String email,
                LocalDateTime created,
                List<Role> roles,
                String country) {

        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.neptunCode = neptunCode;
        this.email = email;
        this.created = created;
        this.roles=roles;
        this.country = country;

    }

    public User() {
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    //@JsonIgnore
    private List<Role> roles = new ArrayList<>();
}
