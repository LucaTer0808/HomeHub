package unit.domain.bookkeeping;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Expense;
import com.terfehr.homehub.domain.bookkeeping.entity.Income;
import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import com.terfehr.homehub.domain.bookkeeping.value.Money;
import com.terfehr.homehub.domain.household.entity.Household;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AccountTest {

    @Test
    public void testValidate() {

        Household household = mock(Household.class);
        Money money = mock(Money.class);

        assertThrows(IllegalArgumentException.class, () -> new Account(null, money, household));
        assertThrows(IllegalArgumentException.class, () -> new Account("", money, household));
        assertThrows(IllegalArgumentException.class, () -> new Account("Valid Name", null, household));
        assertThrows(IllegalArgumentException.class, () -> new Account("Valid Name", money, null));

        assertDoesNotThrow( () -> new Account("Valid Name", money, household));
    }

    @Test
    public void testSetters() {
        Household household = mock(Household.class);
        Money money = mock(Money.class);

        Account account = new Account("Valid Name", money, household);

        assertThrows(IllegalArgumentException.class, () -> account.setName(null));
        assertThrows(IllegalArgumentException.class, () -> account.setName(""));

        assertDoesNotThrow(() -> account.setName("New Valid Name"));
    }

    @Test
    public void testAddTransaction() {
        Household household = mock(Household.class);
        Household household2 = mock(Household.class);
        Currency currency = Currency.getInstance("USD");

        Money money = new Money(currency, 10000); // $100.00
        Money incomeResult = new Money(currency, 11000); // $120.00

        Account account = new Account("Valid Name", money, household);

        Transaction transaction = new Income(1000, "Test Transaction", LocalDateTime.now(), "Source", account);
        Transaction transaction2 = new Expense(1000, "Test Transaction", LocalDateTime.now(), "Recipient", account);
        Transaction transaction3 = new Expense(1000, "Test Transaction", LocalDateTime.now(), "Recipient", new Account("Other Account", money, household2));

        assertThrows(IllegalArgumentException.class, () -> account.addTransaction(null));

        assertDoesNotThrow(() -> account.addTransaction(transaction));
        assertEquals(account.getBalance(), incomeResult);

        assertThrows(IllegalArgumentException.class, () -> account.addTransaction(transaction));

        assertDoesNotThrow(() -> account.addTransaction(transaction2));
        assertEquals(account.getBalance(), money);

        assertThrows(IllegalArgumentException.class, () -> account.addTransaction(transaction3)); // different account

    }

    @Test
    public void testRemoveTransaction() {
        Household household = mock(Household.class);
        Currency currency = Currency.getInstance("USD");

        Money money = new Money(currency, 10000); // $100.00
        Money resultAfterAdding = new Money(currency, 12000);

        Account account = new Account("Valid Name", money, household);

        Transaction transaction = new Income(2000, "Test Transaction", LocalDateTime.now(), "Source", account);
        Transaction transaction2 = new Expense(2000, "Test Transaction", LocalDateTime.now(), "Recipient", account);

        account.addTransaction(transaction);

        assertThrows(IllegalArgumentException.class, () -> account.removeTransaction(null));
        assertThrows(IllegalArgumentException.class, () -> account.removeTransaction(transaction2)); // not added yet

        assertEquals(account.getBalance(), resultAfterAdding);

        account.removeTransaction(transaction);

        assertEquals(account.getBalance(), money);
    }
}
