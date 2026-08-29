package lk.edu.icbt.sunrise.service;

import lk.edu.icbt.sunrise.dao.*;
import lk.edu.icbt.sunrise.exception.BusinessException;
import lk.edu.icbt.sunrise.model.*;
import lk.edu.icbt.sunrise.pattern.factory.ReferenceNumberFactory;
import lk.edu.icbt.sunrise.pattern.strategy.PricingStrategy;
import javax.sql.DataSource;
import java.math.*;

import java.sql.*;

public final class BillingService {
    private final DataSource ds;
    private final AppointmentDao appointments;
    private final TreatmentDao treatments;
    private final BillDao bills;
    private final PricingStrategy pricing;
    private final ReferenceNumberFactory refs;

    public BillingService(DataSource ds, AppointmentDao a, TreatmentDao t, BillDao b, PricingStrategy p, ReferenceNumberFactory r) {
        this.ds = ds;
        appointments = a;
        treatments = t;
        bills = b;
        pricing = p;
        refs = r;
    }

    public Bill issue(String no, BigDecimal discount) {
        if (bills.findByAppointment(no).isPresent()) throw new BusinessException("A bill already exists for this appointment");
        Appointment a = appointments.findByNumber(no).orElseThrow(() -> new BusinessException("Appointment not found"));
        Treatment t = treatments.findById(a.treatmentId()).orElseThrow();
        BigDecimal consultation = new BigDecimal("1500.00");
        BigDecimal total = pricing.calculate(t.price(), consultation, discount);
        try(Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            try {
                Bill b = bills.create(c, new Bill(0, refs.bill(), no, t.price(), consultation, discount, total, BigDecimal.ZERO, PaymentStatus.PENDING, null));
                c.commit();
                return b;
            } catch (RuntimeException x) {
                c.rollback();
                throw x;
            }
        } catch (SQLException e) {
            throw new BusinessException("Billing transaction failed", e);
        }
    }
}
