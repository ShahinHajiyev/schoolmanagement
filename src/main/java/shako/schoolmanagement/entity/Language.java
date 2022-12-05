package shako.schoolmanagement.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "language")
@Data
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private int programId;

    @Column(name = "language_name")
    private String languageName;
}
