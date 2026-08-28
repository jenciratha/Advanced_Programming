package lk.edu.icbt.sunrise.dao.jdbc;

import lk.edu.icbt.sunrise.dao.BillDao;
import lk.edu.icbt.sunrise.model.*;
import javax.sql.DataSource;
import java.sql.*;

import java.util.*;

public final class JdbcBillDao implements BillDao {
    private final DataSource ds;

    public JdbcBillDao(DataSource d) {
        ds = d;
    }

    public Bill create(Connection c, Bill b) {
        String s = "INSERT INTO bills(bill_number,appointment_number,treatment_fee,consultation_fee,discount,total,paid_amount,payment_status,issued_by) VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement p = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, b.billNumber());
            p.setString(2, b.appointmentNumber());
            p.setBigDecimal(3, b.treatmentFee());
            p.setBigDecimal(4, b.consultationFee());
            p.setBigDecimal(5, b.discount());
            p.setBigDecimal(6, b.total());
            p.setBigDecimal(7, b.paidAmount());
            p.setString(8, b.status().name());
            p.setLong(9, 1);
            p.executeUpdate();
            try(ResultSet r = p.getGeneratedKeys()) {
                r.next();
                return new Bill(r.getLong(1), b.billNumber(), b.appointmentNumber(), b.treatmentFee(), b.consultationFee(), b.discount(), b.total(), b.paidAmount(), b.status(), null);
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("issuing bill", e);
        }
    }

    public Optional<Bill> findByAppointment(String no) {
        String s = "SELECT * FROM bills WHERE appointment_number=?";
        try(Connection c = ds.getConnection(); PreparedStatement p = c.prepareStatement(s)) {
            p.setString(1, no);
            try(ResultSet r = p.executeQuery()) {
                return r.next() ? Optional.of(new Bill(r.getLong("id"), r.getString("bill_number"), r.getString("appointment_number"), r.getBigDecimal("treatment_fee"), r.getBigDecimal("consultation_fee"), r.getBigDecimal("discount"), r.getBigDecimal("total"), r.getBigDecimal("paid_amount"), PaymentStatus.valueOf(r.getString("payment_status")), r.getTimestamp("issued_at").toLocalDateTime())) : Optional.empty();
            }
        } catch (SQLException e) {
            throw JdbcSupport.failure("finding bill", e);
        }
    }
}
