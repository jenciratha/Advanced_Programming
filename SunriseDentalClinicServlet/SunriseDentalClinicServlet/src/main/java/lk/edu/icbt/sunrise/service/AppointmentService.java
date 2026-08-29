package lk.edu.icbt.sunrise.service;

import lk.edu.icbt.sunrise.dao.*;
import lk.edu.icbt.sunrise.exception.*;
import lk.edu.icbt.sunrise.model.*;
import lk.edu.icbt.sunrise.pattern.factory.ReferenceNumberFactory;
import lk.edu.icbt.sunrise.pattern.observer.AppointmentObserver;
import javax.sql.DataSource;
import java.sql.*;
import java.time.*;

import java.util.*;

public final class AppointmentService {
    private final DataSource ds;
    private final AppointmentDao appointments;
    private final PatientDao patients;
    private final TreatmentDao treatments;
    private final ReferenceNumberFactory refs;
    private final List<AppointmentObserver> observers;

    public AppointmentService(DataSource d, AppointmentDao a, PatientDao p, TreatmentDao t, ReferenceNumberFactory r, List<AppointmentObserver> o) {
        ds = d;
        appointments = a;
        patients = p;
        treatments = t;
        refs = r;
        observers = List.copyOf(o);
    }

    public Appointment register(String patientName, String address, String contact, String email, long dentistId, long treatmentId, LocalDate date, LocalTime time, String notes) {
        Map<String, String> e = validate(patientName, address, contact, date, time);
        if (!e.isEmpty()) throw new ValidationException(e);
        Treatment tr = treatments.findById(treatmentId).orElseThrow(() -> new BusinessException("Treatment not found"));
        LocalTime end = time.plusMinutes(tr.durationMinutes());
        try(Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            try {
                if (appointments.hasOverlap(c, dentistId, date, time, end)) throw new BusinessException("Selected dentist has an overlapping appointment");
                Patient p = patients.create(c, new Patient(0, refs.patient(), patientName.trim(), address.trim(), contact.trim(), email == null ? null : email.trim(), null));
                Appointment a = appointments.create(c, new Appointment(0, refs.appointment(), p.id(), p.fullName(), dentistId, "", treatmentId, tr.name(), date, time, end, AppointmentStatus.BOOKED, notes, null));
                c.commit();
                observers.forEach(x -> x.created(a));
                return a;
            } catch (RuntimeException ex) {
                c.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new BusinessException("Appointment transaction failed", ex);
        }
    }

    private Map<String, String> validate(String n, String a, String c, LocalDate d, LocalTime t) {
        Map<String, String> e = new LinkedHashMap<>();
        if (n == null || n.isBlank()) e.put("patientName", "Patient name is required");
        if (a == null || a.isBlank()) e.put("address", "Address is required");
        if (c == null || !c.matches("^(?:\+94|0)7\d{8}$")) e.put("contact", "Use 07XXXXXXXX or +947XXXXXXXX");
        if (d == null || d.isBefore(LocalDate.now())) e.put("date", "Date cannot be in the past");
        if (t == null) e.put("time", "Time is required");
        return e;
    }
}
