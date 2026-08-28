package lk.edu.icbt.sunrise.dao;

import lk.edu.icbt.sunrise.model.*;
import java.sql.Connection;
import java.time.*;

import java.util.*;

public interface AppointmentDao {
    boolean hasOverlap(Connection c, long dentistId, LocalDate date, LocalTime start, LocalTime end);
    Appointment create(Connection c, Appointment a);
    Optional<Appointment> findByNumber(String no);
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> search(String q, String status, String date);
    void updateStatus(String no, AppointmentStatus status);
    Map<String, Number> dashboard();
}
