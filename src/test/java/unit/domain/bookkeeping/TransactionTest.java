package unit.domain.bookkeeping;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Income;
import com.terfehr.homehub.domain.bookkeeping.value.Money;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionTest {

    @Test
    public void testValidate() { // just picking income as a subclass of Transaction
        Account account = mock(Account.class);
        Money balance = new Money(Currency.getInstance("USD"), 10000);
        when(account.getBalance()).thenReturn(balance);

        assertThrows(IllegalArgumentException.class, () -> new Income(-10, "Desc", LocalDateTime.now(), "Source", account));
        assertThrows(IllegalArgumentException.class, () -> new Income(100, null, LocalDateTime.now(), "Source", account));
        assertThrows(IllegalArgumentException.class, () -> new Income(100, "Desc", null, "Source", account));
        assertThrows(IllegalArgumentException.class, () -> new Income(100, "Desc", LocalDateTime.now(), "Source", null));
        assertDoesNotThrow(() -> new Income(100, "Desc", LocalDateTime.now(), "Source", account));
    }

    // Only to check if exceptions are thrown for invalid inputs
    @Test
    public void testSetters() {
        Account account = mock(Account.class);
        Money balance = new Money(Currency.getInstance("USD"), 10000);
        when(account.getBalance()).thenReturn(balance);

        Income income = new Income(10000, "Desc", LocalDateTime.now(), "Source", account);

        // Valid cases
        assertDoesNotThrow(() -> income.setAmount(100));
        assertDoesNotThrow(() -> income.setDescription("New Desc"));

        // Invalid cases
        assertThrows(IllegalArgumentException.class, () -> income.setAmount(-100));
        assertThrows(IllegalArgumentException.class, () -> income.setDescription(null));
        assertThrows(IllegalArgumentException.class, () -> income.setDescription(""));
    }
}
