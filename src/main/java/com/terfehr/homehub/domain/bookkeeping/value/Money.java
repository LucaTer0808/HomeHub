package com.terfehr.homehub.domain.bookkeeping.value;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Immutable value object representing a monetary amount in a specific currency.
 * It encapsulates the currency code, the amount in the smallest unit (e.g., cents for USD),
 * and the scale (number of decimal places), as well as providing methods for common monetary operations.
 */
@Value
@Embeddable
@NoArgsConstructor(force = true)

public class Money {

    String currencyCode;
    long amountInSmallestUnit;
    int scale;

    /**
     * Returns the string representation of the Money object in the format "CURRENCY AMOUNT".
     * @return Formatted string representation of the monetary amount.
     */
    public String format() {
        return String.format("%s %.2f", currencyCode, amountInSmallestUnit / Math.pow(10, scale));
    }

}
