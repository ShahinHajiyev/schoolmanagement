package shako.schoolmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    // ToDo : check the studentid, teacherid relationships with other tables if they work or not. Inheritance thing, there is not an id in those classes, only in tables.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseId")
    private int courseId;


    @Column(name = "course_name")
    private String courseName;

    @Column(name = "credit")
    private int credit;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;
}
