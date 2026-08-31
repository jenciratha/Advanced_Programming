package lk.edu.icbt.sunrise.pattern.strategy;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StandardPricingStrategyTest {
    @Test void calculatesTotal() {
        assertEquals(new BigDecimal("6500.00"), new StandardPricingStrategy().calculate(new BigDecimal("5500"), new BigDecimal("1500"), new BigDecimal("500")));
    }
    @Test void neverReturnsNegative() {
        assertEquals(new BigDecimal("0.00"), new StandardPricingStrategy().calculate(new BigDecimal("100"), BigDecimal.ZERO, new BigDecimal("500")));
    }
}
