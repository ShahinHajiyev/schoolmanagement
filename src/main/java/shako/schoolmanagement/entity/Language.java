package shako.schoolmanagement.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "language")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    @EqualsAndHashCode.Include
    private int programId;

    @Column(name = "language_name")
    private String languageName;
}
