package lk.edu.icbt.sunrise.dao.jdbc;

import lk.edu.icbt.sunrise.dao.AppointmentDao;
import lk.edu.icbt.sunrise.model.*;
import javax.sql.DataSource;
import java.sql.*;
import java.time.*;

import java.util.*;

public final class JdbcAppointmentDao implements AppointmentDao {
    private final DataSource ds;

    public JdbcAppointmentDao(DataSource d) {
        ds = d;
    }

    public boolean hasOverlap(Connection c, long d, LocalDate date, LocalTime start, LocalTime end) {
        String s = "SELECT COUNT(*) FROM appointments WHERE dentist_id=? AND appointment_date=? AND status NOT IN('CANCELLED','NO_SHOW') AND appointment_time < ? AND end_time> ?";
        try(PreparedStatement p = c.prepareStatement(s)) {
            p.setLong(1, d);
            p.setDate(2, java.sql.Date.valueOf(date));
            p.setTime(3, Time.valueOf(end));
            p.setTime(4, Time.valueOf(start));
            try(ResultSet r = p.executeQuery()) {
                r.next();
                return r.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("checking appointment overlap", e);
        }
    }

    public Appointment create(Connection c, Appointment a) {
        String s = "INSERT INTO appointments(appointment_number,patient_id,dentist_id,treatment_id,appointment_date,appointment_time,end_time,status,notes,created_by) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement p = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, a.appointmentNumber());
            p.setLong(2, a.patientId());
            p.setLong(3, a.dentistId());
            p.setLong(4, a.treatmentId());
            p.setDate(5, Date.valueOf(a.appointmentDate()));
            p.setTime(6, Time.valueOf(a.appointmentTime()));
            p.setTime(7, Time.valueOf(a.endTime()));
            p.setString(8, a.status().name());
            p.setString(9, a.notes());
            p.setLong(10, 1);
            p.executeUpdate();
            try(ResultSet r = p.getGeneratedKeys()) {
                r.next();
                return new Appointment(r.getLong(1), a.appointmentNumber(), a.patientId(), a.patientName(), a.dentistId(), a.dentistName(), a.treatmentId(), a.treatmentName(), a.appointmentDate(), a.appointmentTime(), a.endTime(), a.status(), a.notes(), null);
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("creating appointment", e);
        }
    }

    public Optional<Appointment> findByNumber(String n) {
        return select("WHERE a.appointment_number=?", List.of(n)).stream().findFirst();
    }

    public List<Appointment> findByDate(LocalDate d) {
        return select("WHERE a.appointment_date=?", List.of(d.toString()));
    }

    public List<Appointment> search(String q, String status, String date) {
        StringBuilder b = new StringBuilder("WHERE 1=1");
        List<String> p = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            b.append(" AND(a.appointment_number LIKE ? OR pt.full_name LIKE ?)");
            p.add("%" + q + "%");
            p.add("%" + q + "%");
        }
        if (status != null && !status.isBlank()) {
            b.append(" AND a.status=?");
            p.add(status);
        }
        if (date != null && !date.isBlank()) {
            b.append(" AND a.appointment_date=?");
            p.add(date);
        }
        return select(b.toString(), p);
    }

    private List<Appointment> select(String where, List<String> vals) {
        String s = "SELECT a.*,pt.full_name patient_name,d.full_name dentist_name,t.name treatment_name FROM appointments a JOIN patients pt ON pt.id=a.patient_id JOIN dentists d ON d.id=a.dentist_id JOIN treatments t ON t.id=a.treatment_id " + where + " ORDER BY a.appointment_date DESC,a.appointment_time DESC LIMIT 200";
        List<Appointment> out = new ArrayList<>();
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement(s)) {
            for (int i = 0; i < vals.size(); i ++) p.setString(i + 1, vals.get(i));
            try(ResultSet r = p.executeQuery()) {
                while (r.next()) out.add(map(r));
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("searching appointments", e);
        }
        return out;
    }

    private Appointment map(ResultSet r) throws SQLException {
        return new Appointment(r.getLong("id"), r.getString("appointment_number"), r.getLong("patient_id"), r.getString("patient_name"), r.getLong("dentist_id"), r.getString("dentist_name"), r.getLong("treatment_id"), r.getString("treatment_name"), r.getDate("appointment_date").toLocalDate(), r.getTime("appointment_time").toLocalTime(), r.getTime("end_time").toLocalTime(), AppointmentStatus.valueOf(r.getString("status")), r.getString("notes"), r.getTimestamp("created_at").toLocalDateTime());
    }

    public void updateStatus(String n, AppointmentStatus st) {
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement("UPDATE appointments SET status=? WHERE appointment_number=?")) {
            p.setString(1, st.name());
            p.setString(2, n);
            if (p.executeUpdate() != 1) throw new IllegalArgumentException("Appointment not found");
        } catch (SQLException e) {
            throw JdbcSupport.failure("updating status", e);
        }
    }

    public Map<String, Number> dashboard() {
        Map<String, Number> m = new LinkedHashMap<>();
        String s = "SELECT COUNT(*) total,SUM(appointment_date=CURDATE()) today,SUM(status='COMPLETED') completed,SUM(status='CANCELLED') cancelled FROM appointments";
        try(Connection c = ds.getConnection(); Statement st = c.createStatement(); ResultSet r = st.executeQuery(s)) {
            r.next();
            m.put("total", r.getLong("total"));
            m.put("today", r.getLong("today"));
            m.put("completed", r.getLong("completed"));
            m.put("cancelled", r.getLong("cancelled"));
        } catch (SQLException e) {
            throw JdbcSupport.failure("loading dashboard", e);
        }
        return m;
    }
}
