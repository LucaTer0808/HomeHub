package unit.domain.bookkeeping;


import com.terfehr.homehub.domain.bookkeeping.value.Money;

import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    @Test
    public void testWithSymbol() {
        Currency usd = Currency.getInstance("USD");
        Currency eur = Currency.getInstance("EUR");
        Currency bhd = Currency.getInstance("BHD"); // 3 decimal places

        Money money = new Money(usd, 12345); // Represents $123.45
        Money money2 = new Money(eur, 6789); // Represents €67.89
        Money money3 = new Money(usd, 10);
        Money money4 = new Money(bhd, 123456789);

        assertEquals("$ 123.45", money.withSymbol());
        assertEquals("€ 67.89", money2.withSymbol());
        assertEquals("$ 0.10", money3.withSymbol());
        assertEquals("BHD 123456.789", money4.withSymbol()); // BHD symbol is same as code
    }

    @Test
    public void testWithCurrencyCode() {
        Currency usd = Currency.getInstance("USD");
        Currency eur = Currency.getInstance("EUR");
        Currency bhd = Currency.getInstance("BHD"); // 3 decimal places

        Money money = new Money(usd, 12345); // Represents $123.45
        Money money2 = new Money(eur, 6789); // Represents €67.89
        Money money3 = new Money(usd, 10);
        Money money4 = new Money(bhd, 123456789);

        assertEquals("USD 123.45", money.withCurrencyCode());
        assertEquals("EUR 67.89", money2.withCurrencyCode());
        assertEquals("USD 0.10", money3.withCurrencyCode());
        assertEquals("BHD 123456.789", money4.withCurrencyCode());
    }

    @Test
    public void testValidate() {
        assertThrows(IllegalArgumentException.class, () -> new Money(null, 1));
        assertDoesNotThrow(() -> new Money(Currency.getInstance("USD"), 0));
    }
}