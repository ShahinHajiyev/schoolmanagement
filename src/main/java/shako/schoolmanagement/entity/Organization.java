package shako.schoolmanagement.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "organization")
@Data
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organizationId")
    private int organizationId;

    @Column(name = "organization_name")
    private String organizationName;
}
