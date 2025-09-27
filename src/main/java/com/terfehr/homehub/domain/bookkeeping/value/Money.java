package com.terfehr.homehub.domain.bookkeeping.value;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

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
     * Formats the monetary amount into a human-readable string representation.
     * The output includes the ISO 4217 currency code and the amount formatted
     * according to the currency's scale (number of decimal places).
     *
     * @return A string representation of the monetary amount in the format
     *         "Currency Code + Amount", where the amount is adjusted based on the currency scale.
     */
    public String format() {
        BigDecimal value = BigDecimal.valueOf(amountInSmallestUnit)
                .movePointLeft(currency.getScale());
        return String.format("%s %s", currency.getCurrencyCode(), value);
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
