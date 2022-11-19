package shako.schoolmanagement.entity;


import lombok.Builder;
import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teacher")
@Data
public class Teacher extends User{

    public Teacher(int userId) {
        super(userId);
    }

    public Teacher() {
    }
}
