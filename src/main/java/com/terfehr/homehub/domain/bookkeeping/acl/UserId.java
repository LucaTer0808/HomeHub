package com.terfehr.homehub.domain.bookkeeping.acl;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Embeddable
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class UserId {
    Long id;
}

