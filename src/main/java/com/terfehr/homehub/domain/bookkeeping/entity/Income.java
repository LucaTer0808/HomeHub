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
        this.source = source;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Income object");
        }
    }

    public boolean validate() {
        return super.validate() && source != null && !source.isEmpty();
    }
}
