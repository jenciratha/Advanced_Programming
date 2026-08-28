package lk.edu.icbt.sunrise.dao.jdbc;

import lk.edu.icbt.sunrise.dao.PatientDao;
import lk.edu.icbt.sunrise.model.Patient;
import javax.sql.DataSource;
import java.sql.*;

import java.util.*;

public final class JdbcPatientDao implements PatientDao {
    private final DataSource ds;

    public JdbcPatientDao(DataSource d) {
        ds = d;
    }

    public Patient create(Connection c, Patient x) {
        String s = "INSERT INTO patients(patient_code,full_name,address,contact_number,email) VALUES(?,?,?,?,?)";
        try(PreparedStatement p = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, x.patientCode());
            p.setString(2, x.fullName());
            p.setString(3, x.address());
            p.setString(4, x.contactNumber());
            p.setString(5, x.email());
            p.executeUpdate();
            try(ResultSet r = p.getGeneratedKeys()) {
                r.next();
                return new Patient(r.getLong(1), x.patientCode(), x.fullName(), x.address(), x.contactNumber(), x.email(), null);
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("creating patient", e);
        }
    }

    public Optional<Patient> findById(long id) {
        return searchInternal("WHERE id=?", String.valueOf(id)).stream().findFirst();
    }

    public List<Patient> search(String q) {
        return searchInternal("WHERE patient_code LIKE ? OR full_name LIKE ? OR contact_number LIKE ?", "%" + q + "%");
    }

    private List<Patient> searchInternal(String where, String v) {
        List<Patient> out = new ArrayList<>();
        String s = "SELECT * FROM patients " + where + " ORDER BY full_name LIMIT 100";
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement(s)) {
            if (where.contains("id=?")) p.setLong(1, Long.parseLong(v));
            else {
                p.setString(1, v);
                p.setString(2, v);
                p.setString(3, v);
            }
            try(ResultSet r = p.executeQuery()) {
                while (r.next()) out.add(new Patient(r.getLong("id"), r.getString("patient_code"), r.getString("full_name"), r.getString("address"), r.getString("contact_number"), r.getString("email"), r.getTimestamp("created_at").toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("searching patients", e);
        }
        return out;
    }
}
