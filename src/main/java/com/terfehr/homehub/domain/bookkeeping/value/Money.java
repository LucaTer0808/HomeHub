package com.terfehr.homehub.domain.bookkeeping.value;

import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

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
        if (!validate(currency)) {
            throw new IllegalArgumentException("Invalid Money object");
        }
        this.currency = currency;
        this.amountInSmallestUnit = amountInSmallestUnit;
    }

    /**
     * Returns a formatted string representation of the monetary amount including the currency symbol
     * and the amount adjusted to the correct decimal places based on the currency's scale.
     *
     * @return A string containing the currency symbol followed by the adjusted monetary amount.
     */
    public String withSymbol() {
        BigDecimal value = BigDecimal.valueOf(amountInSmallestUnit)
                .movePointLeft(currency.getDefaultFractionDigits());
        return String.format("%s %s", currency.getSymbol(), value);
    }

    /**
     * Formats the monetary amount into a human-readable string representation.
     * The output includes the ISO 4217 currency code and the amount formatted
     * according to the currency's scale (number of decimal places).
     *
     * @return A string representation of the monetary amount in the format
     *         "Currency Code + Amount", where the amount is adjusted based on the currency scale.
     */
    public String withCurrencyCode() {
        BigDecimal value = BigDecimal.valueOf(amountInSmallestUnit)
                .movePointLeft(currency.getDefaultFractionDigits());
        return String.format("%s %s", currency.getCurrencyCode(), value);
    }

    /**
     * Validates the given currency to ensure it is not null.
     *
     * @param currency the currency to be validated
     * @return {@code true} if the currency is not null, otherwise {@code false}
     */
    public boolean validate(Currency currency) {
        return currency != null;
    }
}
