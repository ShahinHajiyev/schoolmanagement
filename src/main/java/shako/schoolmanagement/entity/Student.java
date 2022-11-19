package shako.schoolmanagement.entity;

import lombok.Builder;
import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student")
@Data
public class Student extends User{

    @Column(name = "roll_no")
    private int rollNumber;

    @Column(name = "graduation_year")
    private Date graduationYear;

    public Student(int userId, int rollNumber, Date graduationYear) {
        super(userId);
        this.rollNumber = rollNumber;
        this.graduationYear = graduationYear;
    }

    public Student() {

    }
}
