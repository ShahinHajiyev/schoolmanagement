package shako.schoolmanagement.entity;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "teacher")
@Data
public class Teacher extends User{

    public Teacher(int userId, String neptunCode, String email) {
        super(userId, neptunCode, email);
    }

    public Teacher(){
    }
}
