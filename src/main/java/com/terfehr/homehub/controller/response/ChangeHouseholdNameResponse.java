package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.exception.InvalidHouseholdDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangeHouseholdNameResponse {

    private final HouseholdDTO dto;
    private final LocalDateTime timestamp;

    public ChangeHouseholdNameResponse(HouseholdDTO dto) {
        if (dto == null) {
            throw new InvalidHouseholdDTOException("HouseholdDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
