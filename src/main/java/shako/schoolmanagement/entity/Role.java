package shako.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    @ToString.Exclude
    private List<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",
               joinColumns = @JoinColumn(name = "role_id"),
               inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @ToString.Exclude
    private List<Permission> permissions = new ArrayList<>();

/*    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        List<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .collect(Collectors.toList());
        return permissions;
    }*/
}
