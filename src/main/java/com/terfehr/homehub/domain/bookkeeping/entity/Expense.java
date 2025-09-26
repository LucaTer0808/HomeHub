package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Expense extends Transaction{

    private String recipient;

    public Expense(Money amount, String description, LocalDateTime date, String recipient) {
        super(amount, description, date);
        this.recipient = recipient;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Expense object");
        }
    }

    /**
     * Sets the recipient of the expense. It's who the money was paid to.
     * @param recipient The recipient to whom the expense was paid.
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Expense object");
        }
    }
}
