package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
/*@JsonIdentityInfo(
        generator =  ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")*/
public class Student extends User{

    public Student(int userId,
                   String userName,
                   String password,
                   String firstName,
                   String lastName,
                   String neptunCode,
                   String email,
                   LocalDateTime created,
                   //List<Role> roles,
                   String country,
                   int rollNumber,
                   LocalDateTime graduationYear) {

        super(userId, userName, password, firstName, lastName, neptunCode, email, created, /*roles,*/ country);
        this.rollNumber = rollNumber;
        this.graduationYear = graduationYear;
    }

    @Column(name = "roll_no")
    private int rollNumber;

    @Column(name = "graduation_year")
    private LocalDateTime graduationYear;

    public Student(int userId) {
        super(userId);
    }

    public Student() {
      super();
    }


    @OneToMany(mappedBy = "student")
    private Set<Enrollment> courseEnrollments;

    @OneToOne(mappedBy = "student")
    private Training training;
}
