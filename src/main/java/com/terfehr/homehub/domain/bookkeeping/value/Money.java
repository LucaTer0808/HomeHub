package com.terfehr.homehub.domain.bookkeeping.value;

import com.terfehr.homehub.domain.shared.exception.InvalidCurrencyCodeException;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Represents a monetary value composed of an amount and a currency.
 * The amount is stored in the smallest unit of the specified currency (e.g., cents in USD).
 * This class is immutable and provides methods to format the amount with the corresponding
 * currency symbol or ISO 4217 currency code.
 */
@Value
@Embeddable
@NoArgsConstructor(force = true)
public class Money {

    long amountInSmallestUnit;
    Currency currency;

    /**
     * Constructs a new {@code Money} instance with the specified currency and amount.
     * The amount is represented in the smallest unit of the currency (e.g., cents for USD).
     * Throws {@code IllegalArgumentException} if the specified currency is invalid.
     *
     * @param currencyCode The currency of the money object. Must not be null.
     * @param amountInSmallestUnit The monetary amount in the smallest currency unit.
     *                              For example, cents for USD or yen for JPY.
     *                              Can be positive, negative, or zero.
     * @throws InvalidCurrencyCodeException If the currency is null or invalid.
     */
    public Money(String currencyCode, long amountInSmallestUnit) throws InvalidCurrencyCodeException {
        try {
            this.currency = Currency.getInstance(currencyCode);
            this.amountInSmallestUnit = amountInSmallestUnit;
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new InvalidCurrencyCodeException("The given CurrencyCode " + currencyCode + "does not represent a valid currency" );
        }
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
     * @param currencyCode the currency to be validated as a String
     * @return {@code true} if the currency is not null, otherwise {@code false}
     */
    public boolean validate(String currencyCode) {
        return currency != null;
    }
}
