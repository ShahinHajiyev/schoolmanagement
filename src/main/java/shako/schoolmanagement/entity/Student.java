package shako.schoolmanagement.entity;

import lombok.Builder;
import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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
                   int rollNumber,
                   LocalDateTime graduationYear) {

        super(userId, userName, password, firstName, lastName, neptunCode, email);
        this.rollNumber = rollNumber;
        this.graduationYear = graduationYear;

    }

    public Student() {
      super();
    }
}
