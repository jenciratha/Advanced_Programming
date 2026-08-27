package lk.edu.icbt.sunrise.model;

public record User(long id, String username, String passwordHash, String fullName, Role role, boolean active, int failedAttempts) {
}
