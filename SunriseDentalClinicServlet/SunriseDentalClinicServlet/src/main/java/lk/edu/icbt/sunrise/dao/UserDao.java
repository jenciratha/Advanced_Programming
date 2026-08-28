package lk.edu.icbt.sunrise.dao;

import lk.edu.icbt.sunrise.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
    void recordSuccess(long id);
    void recordFailure(long id);
    void create(User user);
    long count();
}
