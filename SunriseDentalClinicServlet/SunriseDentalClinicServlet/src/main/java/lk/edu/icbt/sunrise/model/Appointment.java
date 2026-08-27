package lk.edu.icbt.sunrise.model;

import java.time.*;

public record Appointment(long id, String appointmentNumber, long patientId, String patientName, long dentistId, String dentistName, long treatmentId, String treatmentName, LocalDate appointmentDate, LocalTime appointmentTime, LocalTime endTime, AppointmentStatus status, String notes, LocalDateTime createdAt) {
}
