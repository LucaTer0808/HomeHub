package unit.domain.household;

import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HouseholdTest {

    @Test
    public void testValidate() {
        String name = "testname";

        Household household = new Household(name);

        assertThrows(IllegalArgumentException.class, () -> new Household(null));
        assertThrows(IllegalArgumentException.class, () -> new Household(""));

        assertEquals(name, household.getName());
        assertEquals(0, household.getRoommates().size());
    }

    @Test
    public void testAddRoommate() {

        Household household = new Household("Coole WG");
        Household household2 = new Household("Langweilige WG");

        User user = new User("testuser", "testmail", "password", "code", LocalDateTime.now().plusDays(1));

        Roommate roommate = new Roommate(household, user);
        Roommate roommate2 = new Roommate(household2, user);

        household.addRoommate(roommate);
        assertThrows(IllegalArgumentException.class, () -> household2.addRoommate(roommate));
        assertThrows(IllegalArgumentException.class, () -> household.addRoommate(null));
        assertThrows(IllegalArgumentException.class, () -> household.addRoommate(roommate2)); // fails due to different household

        assertEquals(1, household.getRoommates().size());
    }

    @Test
    public void testRemoveRoommate() {
        Household household = new Household("Coole WG");
        Household household2 = new Household("Langweilige WG");

        User user = new User("testuser", "testmail", "password", "code", LocalDateTime.now().plusDays(1));
        User user2 = new User("testuser2", "testmail", "password", "code", LocalDateTime.now().plusDays(1));

        Roommate roommate = new Roommate(household, user);
        Roommate roommate2 = new Roommate(household2, user);
        Roommate roommate3 = new Roommate(household, user2);

        household.addRoommate(roommate);

        assertThrows(IllegalArgumentException.class, () -> household.removeRoommate(null));
        assertThrows(IllegalArgumentException.class, () -> household.removeRoommate(roommate2));
        assertThrows(IllegalArgumentException.class, () -> household.removeRoommate(roommate)); // fails because only one roommate

        household.addRoommate(roommate3);

        household.removeRoommate(roommate);
        assertEquals(1, household.getRoommates().size());
    }
}
