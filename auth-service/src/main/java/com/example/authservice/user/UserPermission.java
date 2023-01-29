package com.example.authservice.user;

public enum UserPermission {
    ADMIN_READ("ADMIN:READ"),
    ADMIN_WRITE("ADMIN:WRITE"),
    USER_READ("USER:READ"),
    USER_WRITE("USER:WRITE"),
    GUEST("GUEST");
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
