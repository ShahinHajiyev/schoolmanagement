package shako.schoolmanagement.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "training")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id")
    @EqualsAndHashCode.Include
    private int trainingId;

    @Column(name = "number_of_terms")
    private int numberOfTerms;

    @Column(name = "module")
    private String module;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "program_id")
    private Program program;

}
