package lk.edu.icbt.sunrise.model;

import java.math.BigDecimal;

import java.time.LocalDateTime;

public record Bill(long id, String billNumber, String appointmentNumber, BigDecimal treatmentFee, BigDecimal consultationFee, BigDecimal discount, BigDecimal total, BigDecimal paidAmount, PaymentStatus status, LocalDateTime issuedAt) {
}
