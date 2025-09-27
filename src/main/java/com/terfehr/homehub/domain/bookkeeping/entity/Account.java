package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a financial account in the bookkeeping system. An Account can hold multiple Transaction and
 * Income records, allowing users to track their financial activities across different accounts such as checking,
 * savings, or credit cards. <strong>Aggregate</strong> to {@link Transaction} and {@link Income}.
 */
@Entity
@NoArgsConstructor
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Money balance;
}


