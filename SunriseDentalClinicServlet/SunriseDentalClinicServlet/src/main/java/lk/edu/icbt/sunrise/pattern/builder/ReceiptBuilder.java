package lk.edu.icbt.sunrise.pattern.builder;

import lk.edu.icbt.sunrise.model.Bill;

public final class ReceiptBuilder {
    private final StringBuilder s = new StringBuilder();

    public ReceiptBuilder clinic(String n) {
        s.append(n).append("\n");
        return this;
    }

    public ReceiptBuilder bill(Bill b) {
        s.append("Receipt: ").append(b.billNumber()).append("\nAppointment: ").append(b.appointmentNumber()).append("\nTreatment: ").append(b.treatmentFee()).append("\nConsultation: ").append(b.consultationFee()).append("\nDiscount: ").append(b.discount()).append("\nTOTAL: ").append(b.total());
        return this;
    }

    public String build() {
        return s.toString();
    }
}
