package lk.edu.icbt.sunrise.config;

import lk.edu.icbt.sunrise.dao.*;
import lk.edu.icbt.sunrise.dao.jdbc.*;
import lk.edu.icbt.sunrise.pattern.factory.*;
import lk.edu.icbt.sunrise.pattern.observer.*;
import lk.edu.icbt.sunrise.pattern.strategy.*;
import lk.edu.icbt.sunrise.service.*;
import javax.sql.DataSource;

import java.util.List;

public final class ApplicationFactory {
    private static ApplicationFactory instance;
    public final DataSource ds;
    public final UserDao users;
    public final PatientDao patients;
    public final DentistDao dentists;
    public final TreatmentDao treatments;
    public final AppointmentDao appointments;
    public final BillDao bills;
    public final AuthService auth;
    public final AppointmentService appointmentService;
    public final BillingService billingService;
    private ApplicationFactory() {
        ds = DatabaseConfig.get();
        users = new JdbcUserDao(ds);
        patients = new JdbcPatientDao(ds);
        dentists = new JdbcDentistDao(ds);
        treatments = new JdbcTreatmentDao(ds);
        appointments = new JdbcAppointmentDao(ds);
        bills = new JdbcBillDao(ds);
        ReferenceNumberFactory refs = new ReferenceNumberFactory();
        auth = new AuthService(users);
        appointmentService = new AppointmentService(ds, appointments, patients, treatments, refs, List.of(new AuditObserver()));
        billingService = new BillingService(ds, appointments, treatments, bills, new StandardPricingStrategy(), refs);
    }

    public static synchronized ApplicationFactory get() {
        if (instance == null) instance = new ApplicationFactory();
        return instance;
    }
}
