package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.LeaveHouseholdCommand;
import com.terfehr.homehub.application.event.LeaveHouseholdEvent;
import com.terfehr.homehub.application.event.payload.LeaveHouseholdEventPayload;
import com.terfehr.homehub.application.exception.RoommateNotFoundException;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import com.terfehr.homehub.domain.bookkeeping.repository.TransactionRepositoryInterface;
import com.terfehr.homehub.domain.bookkeeping.service.TransactionService;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.exception.InvalidInvitationException;
import com.terfehr.homehub.domain.household.exception.InvalidRoommateException;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.RoommateRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.HouseholdService;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.scheduling.entity.Task;
import com.terfehr.homehub.domain.scheduling.repository.TaskRepositoryInterface;
import com.terfehr.homehub.domain.scheduling.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class LeaveHouseholdService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final HouseholdRepositoryInterface householdRepository;
    private final HouseholdService householdService;
    private final RoommateRepositoryInterface roommateRepository;
    private final TaskRepositoryInterface taskRepository;
    private final TaskService taskService;
    private final TransactionRepositoryInterface transactionRepository;
    private final TransactionService transactionService;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    /**
     * Executes the LeaveHouseholdCommand by fetching the represented Roommate from the Database and then removing it from the Household.
     * If the Roommate is the only Roommate of the Household, the Household will be deleted. If the Roommate is the Admin of the Household, a new Admin is elected.
     *
     * @param cmd The Command to execute. Contains the ID of the Household and the Email of the User to remove from the Household.
     * @throws AuthenticationCredentialsNotFoundException If there is no user in the security context.
     * @throws UserNotFoundException If the User to remove from the Household does not exist.
     * @throws RoommateNotFoundException If the Roommate to remove from the Household does not exist.
     * @throws InvalidInvitationException If the Invitations of the given Household are invalid. Should not occur here since we fetch them directly from the Household.
     * @throws InvalidRoommateException If the given Roommate is not part of the given Household or Invalid. Should not occur here since we fetch them directly from the Household.
     */
    public void execute(LeaveHouseholdCommand cmd) throws
            AuthenticationCredentialsNotFoundException,
            UserNotFoundException,
            RoommateNotFoundException,
            InvalidInvitationException,
            InvalidRoommateException
    {
        User user = userProvider.getUser();
        Roommate roommate = roommateRepository.findByHouseholdIdAndUserEmail(cmd.id(), user.getEmail())
                .orElseThrow(() -> new RoommateNotFoundException("There is no Roommate connecting the Household with ID: " + cmd.id() + " to the User with Email: " + user.getEmail()));
        Household household = roommate.getHousehold();

        updateHousehold(household, roommate);
        updateUser(user, roommate);

        roommateRepository.delete(roommate);

        LeaveHouseholdEventPayload payload = new LeaveHouseholdEventPayload(household.getId(), user.getId());
        LeaveHouseholdEvent event = new LeaveHouseholdEvent(this, payload);
        publisher.publishEvent(event);
    }

    /**
     * Updates the household by making the Roommate leave the household. If the given Roommate is the only member of the household,
     * the household will be deleted. This also includes all Invitations. If the Roommate is the Admin of the Household, a new Admin is elected. In all other cases,
     * the Roommate will be removed from the Household.
     *
     * @param household The household to update.
     * @param roommate The Roommate to remove from the household.
     * @throws InvalidInvitationException If the Invitations of the given Household are invalid. Should not occur here since we fetch them directly from the Household.
     * @throws InvalidRoommateException If the given Roommate is not part of the given Household or Invalid.
     */
    private void updateHousehold(Household household, Roommate roommate) throws InvalidInvitationException, InvalidRoommateException {
        if (household.isLastRoommate()) { // if the user is the last member of the household, he automatically is an admin before deletion
            Set<User> updatedUsers = userService.removeInvitationsByHousehold(household);
            userRepository.saveAll(updatedUsers);
            householdRepository.delete(household);
            return;
        }

        household.removeRoommate(roommate);
        updateTasks(roommate);
        updateTransactions(roommate);
        householdRepository.save(household);
    }

    /**
     * Updates the given User by removing the given roommate from the user and saving the user.
     *
     * @param user The User to update.
     * @param roommate The Roommate to remove from the User.
     */
    private void updateUser(User user, Roommate roommate) throws InvalidRoommateException {
        user.removeRoommate(roommate);
        userRepository.save(user);
    }

    /**
     * Updates all Tasks connected to the given roommate by stripping all Transactions of its roommate and then saving them.
     *
     * @param roommate The roommate to update the tasks for.
     */
    private void updateTasks(Roommate roommate) {
        Set<Task> tasks = taskRepository.findAllByRoommate(roommate);
        taskService.removeRoommates(tasks);
        taskRepository.saveAll(tasks);
    }

    /**
     * Updates all Transactions connected to the given roommate by stripping all Transactions of its roommate and then saving them.
     *
     * @param roommate The roommate to update the tasks for.
     */
    private void updateTransactions(Roommate roommate) {
        Set<Transaction> transactions = transactionRepository.findAllByRoommate(roommate);
        transactionService.removeRoommates(transactions);
        transactionRepository.saveAll(transactions);
    }
}
