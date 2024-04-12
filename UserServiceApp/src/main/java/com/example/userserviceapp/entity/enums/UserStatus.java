package com.example.userserviceapp.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE("active"),
    BLOCKED("blocked");
    private final String status;
    public static UserStatus stringNameToValue(String status) {
        return Stream.of(UserStatus.values())
                .filter(p -> p.getStatus().equals(status.toLowerCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
