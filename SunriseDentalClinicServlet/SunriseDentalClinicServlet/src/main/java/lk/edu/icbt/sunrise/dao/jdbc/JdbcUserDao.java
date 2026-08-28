package lk.edu.icbt.sunrise.dao.jdbc;

import lk.edu.icbt.sunrise.dao.UserDao;
import lk.edu.icbt.sunrise.model.*;
import javax.sql.DataSource;
import java.sql.*;

import java.util.*;

public final class JdbcUserDao implements UserDao {
    private final DataSource ds;

    public JdbcUserDao(DataSource ds) {
        this.ds = ds;
    }

    public Optional<User> findByUsername(String u) {
        String sql = "SELECT id,username,password_hash,full_name,role,active,failed_attempts FROM users WHERE username=?";
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, u);
            try(ResultSet r = p.executeQuery()) {
                return r.next() ? Optional.of(map(r)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("finding user", e);
        }
    }

    public void recordSuccess(long id) {
        update("UPDATE users SET failed_attempts=0,last_login=NOW() WHERE id=?", id);
    }

    public void recordFailure(long id) {
        update("UPDATE users SET failed_attempts=failed_attempts+1 WHERE id=?", id);
    }

    private void update(String s, long id) {
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement(s)) {
            p.setLong(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw JdbcSupport.failure("updating login", e);
        }
    }

    public void create(User u) {
        String s = "INSERT INTO users(username,password_hash,full_name,role,active) VALUES(?,?,?,?,?)";
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement(s)) {
            p.setString(1, u.username());
            p.setString(2, u.passwordHash());
            p.setString(3, u.fullName());
            p.setString(4, u.role().name());
            p.setBoolean(5, u.active());
            p.executeUpdate();
        } catch (SQLException e) {
            throw JdbcSupport.failure("creating user", e);
        }
    }

    public long count() {
        try(Connection c = ds.getConnection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM users")) {
            r.next();
            return r.getLong(1);
        } catch (SQLException e) {
            throw JdbcSupport.failure("counting users", e);
        }
    }

    private User map(ResultSet r) throws SQLException {
        return new User(r.getLong("id"), r.getString("username"), r.getString("password_hash"), r.getString("full_name"), Role.valueOf(r.getString("role")), r.getBoolean("active"), r.getInt("failed_attempts"));
    }
}
