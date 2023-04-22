package shako.schoolmanagement.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "organization")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    @EqualsAndHashCode.Include
    private int organizationId;

    @Column(name = "organization_name")
    private String organizationName;
}
