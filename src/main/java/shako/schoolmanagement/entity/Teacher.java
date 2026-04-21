package shako.schoolmanagement.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "teacher")
@Data
public class Teacher extends User {

    public Teacher(int userId, String neptunCode, String email) {
        super(userId, neptunCode, email);
    }

    public Teacher() {
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "teacher_program",
        joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "program_id", referencedColumnName = "program_id")
    )
    @ToString.Exclude
    private List<Program> programs;
}
