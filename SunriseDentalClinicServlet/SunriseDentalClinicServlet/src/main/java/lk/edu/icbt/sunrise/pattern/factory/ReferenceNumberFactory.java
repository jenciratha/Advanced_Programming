package lk.edu.icbt.sunrise.pattern.factory;

import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.UUID;

public final class ReferenceNumberFactory {
    public String appointment() {
        return "APT-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + token();
    }

    public String patient() {
        return "PAT-" + token();
    }

    public String bill() {
        return "BIL-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + token();
    }

    private String token() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
