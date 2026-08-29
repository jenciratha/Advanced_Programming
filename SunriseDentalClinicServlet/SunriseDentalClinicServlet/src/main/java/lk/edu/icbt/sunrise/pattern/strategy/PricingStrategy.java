package lk.edu.icbt.sunrise.pattern.strategy;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculate(BigDecimal treatmentFee, BigDecimal consultationFee, BigDecimal discount);
}
