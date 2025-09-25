package com.terfehr.homehub.domain.bookkeeping.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Represents a financial account in the bookkeeping system. An Account can hold multiple Expense and
 * Income records, allowing users to track their financial activities across different accounts such as checking,
 * savings, or credit cards. <strong>Aggregate</strong> to {@link Expense} and {@link Income}.
 */
@Entity
@Data
public class Account {
    @Id
    private Long id;

}
