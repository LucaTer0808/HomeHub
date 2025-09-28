package unit.domain.household;

import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoommateTest {

    @Test
    public void testValidate() {
        Household household = new Household("Coole WG");
        User user = new User("testuser", "testmail", "password", "code", LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> new Roommate(null, null));
        assertThrows(IllegalArgumentException.class, () -> new Roommate(household, null));
        assertThrows(IllegalArgumentException.class, () -> new Roommate(null, user));
        assertDoesNotThrow(() -> new Roommate(household, user));
    }
}
