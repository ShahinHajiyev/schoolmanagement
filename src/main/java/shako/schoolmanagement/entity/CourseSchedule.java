package shako.schoolmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "course_schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /** e.g. "MONDAY", "TUESDAY" … */
    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    /** HH:mm format, e.g. "09:00" */
    @Column(name = "start_time", nullable = false)
    private String startTime;

    /** HH:mm format, e.g. "10:30" */
    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Column(name = "room")
    private String room;
}
