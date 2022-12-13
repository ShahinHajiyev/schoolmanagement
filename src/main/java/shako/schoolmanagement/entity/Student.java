package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
@Data
public class Student extends User{

    @Column(name = "roll_no")
    private int rollNumber;

    @Column(name = "graduation_year")
    private LocalDateTime graduationYear;

    public Student(int userId,
                   String userName,
                   String password,
                   String firstName,
                   String lastName,
                   String neptunCode,
                   String email,
                   LocalDateTime created,
                   List<Role> roles,
                   int rollNumber,
                   LocalDateTime graduationYear) {

        super(userId, userName, password, firstName, lastName, neptunCode, email, created, roles);
        this.rollNumber = rollNumber;
        this.graduationYear = graduationYear;

    }

    public Student(int userId) {
        super(userId);
    }

    public Student() {
      super();
    }


    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<Enrollment> courseEnrollments;
}
