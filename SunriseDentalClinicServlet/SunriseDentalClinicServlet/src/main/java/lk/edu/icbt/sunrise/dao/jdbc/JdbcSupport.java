package lk.edu.icbt.sunrise.dao.jdbc;

import java.sql.*;

import lk.edu.icbt.sunrise.exception.BusinessException;

final class JdbcSupport {
    private JdbcSupport() {
    }
    static BusinessException failure(String action, SQLException e) {
        return new BusinessException("Database operation failed while " + action, e);
    }
}
