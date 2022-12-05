package shako.schoolmanagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
                   int rollNumber,
                   LocalDateTime graduationYear) {

        super(userId, userName, password, firstName, lastName, neptunCode, email, created);
        this.rollNumber = rollNumber;
        this.graduationYear = graduationYear;

    }

    public Student(int userId) {
        super(userId);
    }

    public Student() {
      super();
    }
}
