package shako.schoolmanagement.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "program")
@Data
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private int programId;

    @Column(name = "program_name")
    private String programName;

    @Column(name = "program_duration")
    private int programDuration;
}
