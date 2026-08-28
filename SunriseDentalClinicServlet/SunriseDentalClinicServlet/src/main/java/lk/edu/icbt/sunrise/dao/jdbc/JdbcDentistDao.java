package lk.edu.icbt.sunrise.dao.jdbc;

import lk.edu.icbt.sunrise.dao.DentistDao;
import lk.edu.icbt.sunrise.model.Dentist;
import javax.sql.DataSource;
import java.sql.*;

import java.util.*;

public final class JdbcDentistDao implements DentistDao {
    private final DataSource ds;

    public JdbcDentistDao(DataSource d) {
        ds = d;
    }

    public List<Dentist> findActive() {
        List<Dentist> a = new ArrayList<>();
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement("SELECT * FROM dentists WHERE active=1 ORDER BY full_name"); ResultSet r = p.executeQuery()) {
            while (r.next()) a.add(new Dentist(r.getLong("id"), r.getString("dentist_code"), r.getString("full_name"), r.getString("specialization"), r.getString("contact_number"), true));
        } catch (SQLException e) {
            throw JdbcSupport.failure("loading dentists", e);
        }
        return a;
    }
}
