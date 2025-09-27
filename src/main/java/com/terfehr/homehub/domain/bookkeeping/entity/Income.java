package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Income extends Transaction {

    private String source;

    public Income(Money amount, String description, LocalDateTime date, String source) {
        super(amount, description, date);
        this.source = source;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Income object");
        }
    }

    /**
     * Sets the source of the income. It's where the money was received from.
     * @param source The source from which the income was received.
     */
    public void setSource(String source) {
        if (!validateSource(source)) {
            throw new IllegalArgumentException("Invalid source");
        }
        this.source = source;
    }

    /**
     * Validates the state of the Income object. This method ensures that both the validation
     * criteria defined in the superclass Transaction and the specific validation for the
     * Income entity (i.e., the validity of the source) are satisfied.
     *
     * @return true if the Income object is valid, false otherwise.
     */
    public boolean validate() {
        return super.validate() && validateSource(source);
    }

    /**
     * Validates the provided source string to ensure it is neither null nor empty.
     *
     * @param source The source string to be validated.
     * @return true if the source is not null and not empty, false otherwise.
     */
    private boolean validateSource(String source) {
        return source != null && !source.isEmpty();
    }
}
