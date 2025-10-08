package com.terfehr.homehub.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Command object for creating a new ShoppingSpree. It encapsulates all the necessary information for creating a new ShoppingSpree.
 * It is annotated with @Builder to allow for easy construction of the object. Contains information about the household the ShoppingList the Spree comes from,
 * the Account from which the Spree has been paid. The amount how expensive it was, a brief description of the Spree, the Date when it happened and the recipient
 * who received the money.
 */
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateShoppingSpreeCommand {

    private Long shoppingListId;
    private Long accountId;
    private long amount;
    private String description;
    private LocalDateTime date;
    private String recipient;
}
