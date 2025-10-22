package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.exception.InvalidHouseholdDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetHouseholdResponse {

    private final HouseholdDTO dto;
    private final LocalDateTime timestamp;

    public GetHouseholdResponse(HouseholdDTO dto) {
        if (dto == null) {
            throw new InvalidHouseholdDTOException("HouseholdDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
