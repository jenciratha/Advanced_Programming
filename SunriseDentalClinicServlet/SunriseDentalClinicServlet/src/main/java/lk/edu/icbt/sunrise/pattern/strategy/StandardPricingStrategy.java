package lk.edu.icbt.sunrise.pattern.strategy;

import java.math.*;

public final class StandardPricingStrategy implements PricingStrategy {
    public BigDecimal calculate(BigDecimal t, BigDecimal c, BigDecimal d) {
        if (t.signum() < 0 || c.signum() < 0 || d.signum() < 0) throw new IllegalArgumentException("Amounts cannot be negative");
        BigDecimal total = t.add(c).subtract(d);
        return total.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
}
