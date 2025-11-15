package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.shared.exception.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "incomes")
public class Income extends Transaction {

    @Column(nullable = false)
    private String source;

    /**
     * Constructs an Income object representing a transaction in the bookkeeping system.
     * Includes details about the amount, description, date, source, and associated account.
     * Validates the recipient to ensure data integrity.
     *
     * @param amount The monetary value of the source in the smallest currency unit (e.g., cents for USD).
     * @param description A brief description of the source.
     * @param date The date and time the source occurred.
     * @param source The source to whom the source was paid.
     * @param account The account associated with the source transaction.
     * @param roommate The roommate associated with the source transaction.
     * @throws IllegalArgumentException if the source is invalid.
     */
    public Income(long amount, String description, LocalDateTime date, String source, Account account, Roommate roommate) throws
            InvalidAmountException,
            InvalidDescriptionException,
            InvalidDateException,
            InvalidAccountException,
            InvalidRoommateException
    {
        super(amount, description, date, account, roommate);
        if (!validateSource(source)) {
            throw new InvalidSourceException("Invalid Income object");
        }
        this.source = source;
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
