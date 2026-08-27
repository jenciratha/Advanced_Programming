package lk.edu.icbt.sunrise.model;

import java.math.BigDecimal;

public record Treatment(long id, String treatmentCode, String name, String description, BigDecimal price, int durationMinutes, boolean active) {
}
