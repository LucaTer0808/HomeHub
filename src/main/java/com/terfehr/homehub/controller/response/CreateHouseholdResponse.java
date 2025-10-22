package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.exception.InvalidHouseholdDTOException;
import com.terfehr.homehub.controller.request.CreateHouseholdRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateHouseholdResponse {

    private final HouseholdDTO dto;
    private final LocalDateTime timestamp;

    public CreateHouseholdResponse(HouseholdDTO dto) {
        if (dto == null) {
            throw new InvalidHouseholdDTOException("HouseholdDTO can not be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
