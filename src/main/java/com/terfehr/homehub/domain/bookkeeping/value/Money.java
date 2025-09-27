package com.terfehr.homehub.domain.bookkeeping.value;

import jakarta.persistence.Embeddable;
import lombok.Getter;
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

    long amountInSmallestUnit;
    Currency currency;

    /**
     * Constructor to create a Money object.
     * @param currencyCode ISO 4217 currency code (e.g., "USD", "EUR").
     * @param amountInSmallestUnit Amount in the smallest currency unit (e.g., cents for USD).
     * @param scale Number of decimal places (e.g., 2 for USD).
     */
    public Money(Currency currency, long amountInSmallestUnit) {
        this.currency = currency;
        this.amountInSmallestUnit = amountInSmallestUnit;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Money object");
        }
    }

    /**
     * Returns the string representation of the Money object in the format "CURRENCY AMOUNT".
     * @return Formatted string representation of the monetary amount.
     */
    public String format() {
        return String.format("%s %.2f", currency.getCurrencyCode(), amountInSmallestUnit / Math.pow(10, currency.getScale()));
    }

    /**
     * Validates the Money object to ensure it has a valid currency code and non-negative scale.
     * @return True if the Money object is valid, false otherwise.
     */
    public boolean validate() {
        return currency != null && validateAmountInSmallestUnit(amountInSmallestUnit);
    }

    /**
     * Validates whether the provided amount in the smallest currency unit is non-negative.
     *
     * @param amountInSmallestUnit The amount in the smallest currency unit (e.g., cents for USD) to validate.
     * @return True if the amount is non-negative; false otherwise.
     */
    private boolean validateAmountInSmallestUnit(long amountInSmallestUnit) {
        return amountInSmallestUnit >= 0;
    }

}
