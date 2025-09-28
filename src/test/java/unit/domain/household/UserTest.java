package unit.domain.household;

import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testValidate() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "testmail", "password", "code", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("", "testmail", "password", "code", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", null, "password", "code", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", "", "password", "code", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", "testmail", null, "code", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", "testmail", "", "code", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", "testmail", "password", null, LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", "testmail", "password", "", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", "testmail", "password", "code", null));
        assertThrows(IllegalArgumentException.class, () -> new User("testuser", "testmail", "password", "code", LocalDateTime.now().minusDays(1)));
    }


    @Test
    public void testAddRoommate() {

        User user = new User("testuser", "testmail", "password", "code", LocalDateTime.now().plusDays(1));
        User user2 = new User("testuser2", "testmail2", "password2", "code2", LocalDateTime.now().plusDays(1));

        Household household = new Household("Coole WG");

        Roommate roommate = new Roommate(household, user);
        Roommate roommate2 = new Roommate(household, user2);

        assertDoesNotThrow(() -> user.addRoommate(roommate));

        assertThrows(IllegalArgumentException.class, () -> user.addRoommate(null));
        assertThrows(IllegalArgumentException.class, () -> user.addRoommate(roommate2)); // fails due to different user
        assertThrows(IllegalArgumentException.class, () -> user.addRoommate(roommate)); // fails due to already added

        assertEquals(1, user.getRoommates().size());
    }

    @Test
    public void testRemoveRoommate() {
        User user = new User("testuser", "testmail", "password", "code", LocalDateTime.now().plusDays(1));
        User user2 = new User("testuser2", "testmail2", "password2", "code2", LocalDateTime.now().plusDays(1));

        Household household = new Household("Coole WG");

        Roommate roommate = new Roommate(household, user);
        Roommate roommate2 = new Roommate(household, user2);

        assertDoesNotThrow(() -> user.addRoommate(roommate));
        assertDoesNotThrow(() -> user.removeRoommate(roommate));

        assertThrows(IllegalArgumentException.class, () -> user.removeRoommate(null));
        assertThrows(IllegalArgumentException.class, () -> user.removeRoommate(roommate)); // fails due to already removed
        assertThrows(IllegalArgumentException.class, () -> user.removeRoommate(roommate2)); // fails due to different user

        assertEquals(0, user.getRoommates().size());
    }

    @Test
    public void testEnable() {
        User user = new User("testuser", "testmail", "password", "code", LocalDateTime.now().plusDays(1));
        assertFalse(user.isEnabled());
        assertDoesNotThrow(user::enable);
        assertTrue(user.isEnabled());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationCodeExpiration());

        assertThrows(IllegalStateException.class, user::enable); // fails due to already enabled

        User user2 = new User("testuser2", "testmail2", "password2", "code2", LocalDateTime.now().minusDays(1));
        assertThrows(IllegalStateException.class, user2::enable); // fails due to expired code
    }

    @Test
    public void testGetAuthorities() {
        User user = new User("testuser", "testmail", "password", "code", LocalDateTime.now().plusDays(1));
        assertNotNull(user.getAuthorities());
        assertTrue(user.getAuthorities().isEmpty());
    }
}
