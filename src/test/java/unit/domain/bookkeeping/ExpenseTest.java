package unit.domain.bookkeeping;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Expense;
import com.terfehr.homehub.domain.bookkeeping.value.Money;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenseTest {

    @Test
    public void testValidate() {
        Account account = mock(Account.class);
        Money balance = new Money(Currency.getInstance("USD"), 10000);
        when(account.getBalance()).thenReturn(balance);

        assertThrows(IllegalArgumentException.class, () -> new Expense(100, "Desc", LocalDateTime.now(), "", account));
        assertThrows(IllegalArgumentException.class, () -> new Expense(100, "Desc", LocalDateTime.now(), null, account));
        assertDoesNotThrow(() -> new Expense(100, "Desc", LocalDateTime.now(), "Recipient", account));
    }

    @Test
    // Only to check if exceptions are thrown for invalid inputs
    public void testSetters() {
        Account account = mock(Account.class);
        Money balance = new Money(Currency.getInstance("USD"), 10000);
        when(account.getBalance()).thenReturn(balance);

        Expense expense = new Expense(100, "Desc", LocalDateTime.now(), "Recipient", account);

        assertDoesNotThrow(() -> expense.setRecipient("new test recipient"));

        assertThrows(IllegalArgumentException.class, () -> expense.setRecipient(null));
        assertThrows(IllegalArgumentException.class, () -> expense.setRecipient(""));
    }
}

