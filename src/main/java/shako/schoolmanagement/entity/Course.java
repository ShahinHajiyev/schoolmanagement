package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@SQLDelete(sql = "UPDATE course SET deleted_at = NOW() WHERE course_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Course {

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

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Enrollment> enrollments;

    @OneToOne(mappedBy = "course")
    private CourseDetails courseDetails;

    /** Soft-delete timestamp — NULL means the course is active. */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
