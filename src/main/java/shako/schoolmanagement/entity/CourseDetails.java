package shako.schoolmanagement.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "course_details")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CourseDetails {

    @Column(name = "course_details_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseDetailsId;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "exam_type")
    private String examType;

    @Column(name = "course_type")
    private String courseType;

    @Column(name = "class_schedule")
    private String classSchedule;

    @Column(name = "internet_address")
    private String internetAddress;

    @Column(name = "course_price")
    private int coursePrice;

    @Column(name = "min_headcount")
    private int minHeadcount;

    @Column(name = "max_headcount")
    private int maxHeadcount;

    @Column(name = "preliminary_requirement")
    private String preliminaryRequirement;

    @Column(name = "classes_per_week")
    private int classPerWeek;

    @Column(name = "classes_per_term")
    private int classPerTerm;

    @OneToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @OneToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course;
}
