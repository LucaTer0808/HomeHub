package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteUserCommand;
import com.terfehr.homehub.application.event.DeleteUserEvent;
import com.terfehr.homehub.application.event.payload.DeleteUserEventPayload;
import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import com.terfehr.homehub.domain.bookkeeping.repository.TransactionRepositoryInterface;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Invitation;
import com.terfehr.homehub.domain.household.exception.InvalidPasswordException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.HouseholdService;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.scheduling.entity.Task;
import com.terfehr.homehub.domain.scheduling.repository.TaskRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class DeleteUserService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final HouseholdRepositoryInterface householdRepository;
    private final HouseholdService householdService;
    private final TaskRepositoryInterface taskRepository;
    private final TransactionRepositoryInterface transactionRepository;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    /**
     * Command to execute the deletion of a User. All unfinished tasks that belong to the user will be stripped of the reference to the user. Same goes for the ShoppingSprees
     * and Expenses done by the User. The entities themselves will NOT be deleted since they remain important for the bookkeeping of the household. Additionally,
     * if a User is the only User in the household, the household will be deleted as well. If he is the admin of a household, the admin status will be transferred to
     * the first User in the household. All Invitations connected to the User will be deleted as well.
     *
     * @param cmd The Command to execute.
     */
    public void execute(DeleteUserCommand cmd) {
        User user = userProvider.getUser();

        if (!userService.doesPasswordMatch(user, cmd.password())) {
            throw new InvalidPasswordException("The password you entered is incorrect. The deletion will be canceled.");
        }

        Set<Roommate> roommates = user.getRoommates();
        Set<Invitation> invitations = user.getInvitations();

        updateTransactions(roommates);
        updateTasks(roommates);
        deleteDeletableHouseholds(roommates);
        updateHouseholds(roommates, invitations);

        userRepository.delete(user); // due to cascadeType.ALL, the roommates AND invitations will be deleted as well.

        DeleteUserEventPayload payload = new DeleteUserEventPayload(user.getEmail(), user.getUsername());
        DeleteUserEvent event = new DeleteUserEvent(this, payload);
        publisher.publishEvent(event);
    }

    /**
     * Updates all Transactions that are associated with one of the given roommates.
     *
     * @param roommates The roommates to be stripped off the transactions.
     */
    private void updateTransactions(Set<Roommate> roommates) {
        Set<Transaction> transactions = getAffectedTransactions(roommates);
        transactions.forEach(Transaction::removeRoommate);
        transactionRepository.saveAll(transactions);
    }

    /**
     * Updates all Tasks that are associated with one of the given roommates.
     *
     * @param roommates The roommates to be stripped off the tasks.
     */
    private void updateTasks(Set<Roommate> roommates) {
        Set<Task> changedTasks = getAffectedTasks(roommates);
        changedTasks.forEach(Task::removeRoommate);
        taskRepository.saveAll(changedTasks);
    }

    /**
     * Deletes all Households that are associated with one of the given roommates where the Roommate
     * is the only Roommate left in the household. Makes the whole household deletable and also removes the Invitations
     * from the invited Users.
     *
     * @param roommates The roommates to get the Households for.
     */
    private void deleteDeletableHouseholds(Set<Roommate> roommates) {
        Set<Household> deletableHouseholds = getHouseholdsWithLastRoommate(roommates);

        Set<User> affectedUsers = deletableHouseholds.stream()
                .map(householdService::removeInvitationsByHousehold)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        userRepository.saveAll(affectedUsers);
        householdRepository.deleteAll(deletableHouseholds);
    }

    /**
     * Updates all Households that are associated with one of the given roommates OR Invitations.
     * If the Roommate is the Admin of the Household, its admin rights are transferred after being deleted from the Household.
     *
     * @param roommates The roommates to get the Households for.
     * @param invitations The invitations to get the Households for.
     */
    private void updateHouseholds(Set<Roommate> roommates, Set<Invitation> invitations) {
        Set<Roommate> householdsWhereRoommateIsAdmin = getRoommatesFromFilledHouseholds(roommates);
        Set<Household> households = new HashSet<>();

        households.addAll(
                householdsWhereRoommateIsAdmin.stream()
                        .peek(householdService::leaveHousehold)
                        .map(Roommate::getHousehold)
                        .collect(Collectors.toSet())
        );

        households.addAll(
                invitations.stream()
                        .peek(householdService::deleteInvitationFromHousehold)
                        .map(Invitation::getHousehold)
                        .collect(Collectors.toSet())
        );

        householdRepository.saveAll(households);
    }

    /**
     * Gets all transactions associated with the given roommates.
     *
     * @param roommates The roommates to get the transactions for.
     * @return The set of transactions associated with the given roommates.
     */
    private Set<Transaction> getAffectedTransactions(Set<Roommate> roommates) {
        return roommates.stream()
                .flatMap(r -> transactionRepository.findAllByRoommate(r).stream())
                .collect(Collectors.toSet());
    }

    /**
     * Gets all tasks associated with the given roommates.
     *
     * @param roommates The roommates to get the tasks for.
     * @return The set of tasks associated with the given roommates.
     */
    private Set<Task> getAffectedTasks(Set<Roommate> roommates) {
        return roommates.stream()
                .flatMap(r -> taskRepository.findAllByRoommate(r).stream())
                .collect(Collectors.toSet());
    }

    /**
     * Gets all the Households associated with the given roommates that have only one roommate, which makes them deletable.
     *
     * @param roommates The roommates to get the Households for.
     * @return The set of Households associated with the given roommates.
     */
    private Set<Household> getHouseholdsWithLastRoommate(Set<Roommate> roommates) {
        return roommates.stream()
                .map(Roommate::getHousehold)
                .filter(Household::isLastRoommate)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all Roommates from the Household where said Household is not the last Roommate.
     *
     * @param roommates The roommates to get the Households for.
     * @return All Roommates from the Households where said Household is not the last Roommate.
     */
    private Set<Roommate> getRoommatesFromFilledHouseholds(Set<Roommate> roommates) {
        return roommates.stream()
                .filter(r -> !r.getHousehold().isLastRoommate())
                .collect(Collectors.toSet());
    }
}
