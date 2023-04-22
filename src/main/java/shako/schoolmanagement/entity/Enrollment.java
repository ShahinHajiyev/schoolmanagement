    package shako.schoolmanagement.entity;

    import com.fasterxml.jackson.annotation.*;
    import lombok.*;
    import javax.persistence.*;
    import java.time.LocalDateTime;
    import java.util.Date;


    @Entity
    @Table(name = "enrollment")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    /*@JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "enrollmentId")*/
    public class Enrollment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "enrollment_id")
        @EqualsAndHashCode.Include
        private int enrollmentId;

        @ManyToOne
        @JoinColumn(name = "student_id")
        private Student student;

        @ManyToOne()
        @JoinColumn(name = "course_id")
        private Course course;

        @Column(name = "registered_at")
        private LocalDateTime dateOfRegister;

        @Column(name = "grade")
        private int grade;
    }
