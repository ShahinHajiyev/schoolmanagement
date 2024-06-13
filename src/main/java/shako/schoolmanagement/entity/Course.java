package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "courseId")
public class Course {

    // ToDo : check the studentid, teacherid relationships with other tables if they work or not. Inheritance thing, there is not an id in those classes, only in tables.


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "credit")
    private int credit;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    @ToString.Exclude
    private Semester semester;


    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Enrollment> enrollments;

}
