package lk.edu.icbt.sunrise.service;

import lk.edu.icbt.sunrise.dao.UserDao;
import lk.edu.icbt.sunrise.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public final class AuthService {
    private final UserDao users;

    public AuthService(UserDao u) {
        users = u;
    }

    public Optional<User> authenticate(String name, String password) {
        Optional<User> found = users.findByUsername(name);
        if (found.isEmpty()) return Optional.empty();
        User u = found.get();
        if (!u.active() || u.failedAttempts() >= 5) return Optional.empty();
        if (BCrypt.checkpw(password, u.passwordHash())) {
            users.recordSuccess(u.id());
            return Optional.of(u);
        }
        users.recordFailure(u.id());
        return Optional.empty();
    }
}
