package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Table(name = "semester")
@Entity
@Data
@EqualsAndHashCode
/*@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")*/
public class Semester {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "semester")
    @EqualsAndHashCode.Exclude
    private List<Course> courses;

    @OneToMany(mappedBy = "semester")
    @EqualsAndHashCode.Exclude
    private List<Student> students;

}
