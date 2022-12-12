package shako.schoolmanagement.config.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shako.schoolmanagement.entity.Permission;
import shako.schoolmanagement.entity.Role;
import shako.schoolmanagement.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppUserDetails implements UserDetails {

    private final List<? extends GrantedAuthority> grantedAuthorities;
    private final String password;
    private final String username;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    List<String> roles;

    List<String> permissions;

    public AppUserDetails(User user) {

        this.roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());

        this.permissions = user.getRoles().stream()
                .map(Role::getPermissions)
                .flatMap(permissionList -> permissionList.stream().map(Permission::getPermissionName))
                .collect(Collectors.toList());


        //this.grantedAuthorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.grantedAuthorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.password = user.getPassword();
        this.username = user.getUserName();
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = user.getIsActive();

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
