package com.terfehr.homehub.domain.bookkeeping.value;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Immutable value object representing a currency.
 * A currency consists of an ISO 4217 currency code and a scale,
 * which represents the number of decimal places used for monetary values in that currency.
 */
@Value
@Embeddable
@NoArgsConstructor(force = true)
public class Currency {

    String currencyCode;
    int scale;

    /**
     * Constructor to create a Currency object.
     *
     * @param currencyCode ISO 4217 currency code representing the currency (e.g., "USD", "EUR").
     *                     Must be non-null and not empty.
     * @param scale The number of decimal places used for monetary values in this currency.
     *              Must be a non-negative integer.
     * @throws IllegalArgumentException if the currencyCode or scale is invalid.
     */
    public Currency(String currencyCode, int scale) {
        this.currencyCode = currencyCode;
        this.scale = scale;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Currency object");
        }
    }

    /**
     * Validates the Currency object to ensure it has a valid ISO 4217 currency code and a non-negative scale.
     *
     * @return True if both the currency code and scale are valid, false otherwise.
     */
    public boolean validate() {
        return validateCurrencyCode(currencyCode) && validateScale(scale);
    }

    /**
     * Validates if the provided currency code is non-null and not empty.
     *
     * @param code The ISO 4217 currency code to validate.
     * @return True if the currency code is valid; false otherwise.
     */
    private boolean validateCurrencyCode(String code) {
        return code != null && !code.isEmpty();
    }

    /**
     * Validates if the provided scale is non-negative.
     *
     * @param scale The scale value to validate, representing the number of decimal places.
     * @return True if the scale is non-negative; false otherwise.
     */
    private boolean validateScale(int scale) {
        return scale >= 0;
    }
}
