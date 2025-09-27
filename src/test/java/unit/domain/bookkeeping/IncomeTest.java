package unit.domain.bookkeeping;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Income;
import com.terfehr.homehub.domain.bookkeeping.value.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IncomeTest {

    @Test
    public void testValidate() {
        Account account = mock(Account.class);
        Money balance = new Money(Currency.getInstance("USD"), 10000);
        when(account.getBalance()).thenReturn(balance);

        assertThrows(IllegalArgumentException.class, () -> new Income(100, "Desc", LocalDateTime.now(), "", account));
        assertThrows(IllegalArgumentException.class, () -> new Income(100, "Desc", LocalDateTime.now(), null, account));
        assertDoesNotThrow(() -> new Income(100, "Desc", LocalDateTime.now(), "Source", account));
    }

    @Test
    // Only to check if exceptions are thrown for invalid inputs
    public void testSetters() {
        Account account = mock(Account.class);
        Money balance = new Money(Currency.getInstance("USD"), 10000);
        when(account.getBalance()).thenReturn(balance);

        Income income = new Income(100, "Desc", LocalDateTime.now(), "Source", account);

        assertDoesNotThrow(() -> income.setSource("new test source"));

        assertThrows(IllegalArgumentException.class, () -> income.setSource(null));
        assertThrows(IllegalArgumentException.class, () -> income.setSource(""));
    }
}
