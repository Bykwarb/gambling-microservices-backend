package com.example.authservice.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    GUEST(Set.of(UserPermission.GUEST)),
    USER(Set.of(UserPermission.USER_WRITE, UserPermission.USER_READ)),
    ADMIN(Set.of(UserPermission.ADMIN_WRITE, UserPermission.ADMIN_READ, UserPermission.USER_READ, UserPermission.USER_WRITE));

    Set<UserPermission> permissions;

    Role(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }
    public Set<GrantedAuthority> getAuthorities() {
        return this.permissions.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toSet());
    }
}
