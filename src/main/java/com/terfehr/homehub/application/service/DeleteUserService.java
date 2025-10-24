package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteUserCommand;
import com.terfehr.homehub.application.dto.DeleteUserDTO;
import com.terfehr.homehub.application.event.DeleteUserEvent;
import com.terfehr.homehub.application.event.payload.DeleteUserEventPayload;
import com.terfehr.homehub.application.exception.InvalidPasswordException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.bookkeeping.service.TransactionService;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.HouseholdService;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.scheduling.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class DeleteUserService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final HouseholdService householdService;
    private final TaskService taskService;
    private final TransactionService transactionService;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    /**
     * Command to execute the deletion of a User. All unfinished tasks that belong to the user will be stripped of the reference to the user. Same goes for the ShoppingSprees
     * and Expenses done by the User. The entities themselves will NOT be deleted since they remain important for the bookkeeping of the household. Additionally,
     * if a User is the only User in the household, the household will be deleted as well. If he is the admin of a household, the admin status will be transferred to
     * the first User in the household.
     *
     * @param cmd The Command to execute.
     * @return A DeleteUserDTO containing the deleted User as a DTO.
     */
    public DeleteUserDTO execute(DeleteUserCommand cmd) {
        User user = userProvider.getUser();

        if (!userService.doesPasswordMatch(user, cmd.password())) {
            throw new InvalidPasswordException("The password you entered is incorrect. The deletion will be canceled.");
        }

        Set<Roommate> roommates = user.getRoommates();

        transactionService.setNullByRoommates(roommates);
        taskService.setNullByRoommates(roommates);
        householdService.deleteRoommatesFromHouseholds(roommates);

        userRepository.delete(user); // due to cascadeType.ALL, the roommates will be deleted as well.

        DeleteUserEventPayload payload = new DeleteUserEventPayload(user.getEmail(), user.getUsername());
        DeleteUserEvent event = new DeleteUserEvent(this, payload);
        publisher.publishEvent(event);

        return new DeleteUserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
