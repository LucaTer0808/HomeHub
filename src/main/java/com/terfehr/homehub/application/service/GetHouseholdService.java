package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.GetHouseholdCommand;
import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetHouseholdService {

    private final HouseholdRepositoryInterface householdRepository;

    /**
     * Executes the given GetHouseholdCommand by fetching the represented Household from the Database and returning it as a HouseholdDTO.
     *
     * @param cmd The Command to execute. Contains the ID of the Household to fetch.
     * @return The HouseholdDTO containing the fetched Household.
     * @throws HouseholdNotFoundException If the Household to fetch does not exist.
     */
    public HouseholdDTO execute(GetHouseholdCommand cmd) throws HouseholdNotFoundException{

        Household household = householdRepository.findById(cmd.id())
                .orElseThrow(() -> new HouseholdNotFoundException("Household not found with id: " + cmd.id()));

        return new HouseholdDTO(household.getId(), household.getName());
    }
}
