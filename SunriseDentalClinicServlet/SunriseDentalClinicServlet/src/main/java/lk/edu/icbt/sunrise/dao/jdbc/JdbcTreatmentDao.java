package lk.edu.icbt.sunrise.dao.jdbc;

import lk.edu.icbt.sunrise.dao.TreatmentDao;
import lk.edu.icbt.sunrise.model.Treatment;
import javax.sql.DataSource;
import java.sql.*;

import java.util.*;

public final class JdbcTreatmentDao implements TreatmentDao {
    private final DataSource ds;

    public JdbcTreatmentDao(DataSource d) {
        ds = d;
    }

    public List<Treatment> findActive() {
        return query(null);
    }

    public Optional<Treatment> findById(long id) {
        return query(id).stream().findFirst();
    }

    private List<Treatment> query(Long id) {
        List<Treatment> a = new ArrayList<>();
        String s = "SELECT * FROM treatments WHERE active=1" +(id == null ? " ORDER BY name" : " AND id=?");
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement(s)) {
            if (id != null) p.setLong(1, id);
            try(ResultSet r = p.executeQuery()) {
                while (r.next()) a.add(new Treatment(r.getLong("id"), r.getString("treatment_code"), r.getString("name"), r.getString("description"), r.getBigDecimal("price"), r.getInt("duration_minutes"), true));
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("loading treatments", e);
        }
        return a;
    }
}
