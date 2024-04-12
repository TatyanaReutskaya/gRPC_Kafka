package com.example.libraryserviceapp.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum BookStatus {
    FREE("free"),
    ASSIGNED("assigned");
    private final String status;
    public static BookStatus stringNameToValue(String status) {
        return Stream.of(BookStatus.values())
                .filter(p -> p.getStatus().equals(status.toLowerCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
