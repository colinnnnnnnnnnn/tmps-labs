package com.tmps.lab2.domain;

public class UserSession {
    private final String username;
    private final UserRole role;
    private final boolean authenticated;

    public UserSession(String username, UserRole role, boolean authenticated) {
        this.username = username;
        this.role = role;
        this.authenticated = authenticated;
    }

    public static UserSession guest() {
        return new UserSession("Anonymous Guest", UserRole.GUEST, false);
    }

    public static UserSession customer(String username) {
        return new UserSession(username, UserRole.CUSTOMER, true);
    }

    public static UserSession staff(String username) {
        return new UserSession(username, UserRole.STAFF, true);
    }

    public static UserSession admin(String username) {
        return new UserSession(username, UserRole.ADMIN, true);
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public String toString() {
        return String.format("%s (Role: %s, Authenticated: %b)", username, role, authenticated);
    }
}
