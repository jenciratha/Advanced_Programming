package lk.edu.icbt.sunrise.model;

import java.time.LocalDateTime;

public record Patient(long id, String patientCode, String fullName, String address, String contactNumber, String email, LocalDateTime createdAt) {
}
