package lk.edu.icbt.sunrise.pattern.observer;

import lk.edu.icbt.sunrise.model.Appointment;

import org.slf4j.*;

public final class AuditObserver implements AppointmentObserver {
    private static final Logger LOG = LoggerFactory.getLogger(AuditObserver.class);
    public void created(Appointment a) {
        LOG.info("Appointment created: {} for patient id {}", a.appointmentNumber(), a.patientId());
    }
}
