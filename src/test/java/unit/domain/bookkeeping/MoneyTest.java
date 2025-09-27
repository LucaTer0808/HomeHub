package unit.domain.bookkeeping;


import com.terfehr.homehub.domain.bookkeeping.value.Currency;
import com.terfehr.homehub.domain.bookkeeping.value.Money;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    @Test
    public void testFormat() {
        Currency currency = new Currency("USD", 2);
        Money money = new Money(currency, 12345); // Represents $123.45
        String formatted = money.format();
        assertEquals("USD 123.45", formatted);

        Money money2 = new Money(currency, 100); // Represents $1.00
        String formatted2 = money2.format();
        assertEquals("USD 1.00", formatted2);

        Money money3 = new Money(currency, 1);
        String formatted3 = money3.format();
        assertEquals("USD 0.01", formatted3);
    }
}
