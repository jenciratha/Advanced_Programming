package lk.edu.icbt.sunrise.pattern.observer;

import lk.edu.icbt.sunrise.model.Appointment;

public interface AppointmentObserver {
    void created(Appointment appointment);
}
