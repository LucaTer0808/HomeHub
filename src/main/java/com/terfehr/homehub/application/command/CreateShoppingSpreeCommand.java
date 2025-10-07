package com.terfehr.homehub.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Command object for creating a new ShoppingSpree. It encapsulates all the necessary information for creating a new ShoppingSpree.
 * It is annotated with @Builder to allow for easy construction of the object.
 *
 */
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateShoppingSpreeCommand {

    private Long householdId;
    private Long shoppingListId;
    private Long accountId;
    private long amount;
    private String description;
    private LocalDateTime date;
    private String recipient;
}
