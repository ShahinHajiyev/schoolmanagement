package shako.schoolmanagement.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "program")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "program_id")
    private int programId;

    @Column(name = "program_name")
    private String programName;
}
