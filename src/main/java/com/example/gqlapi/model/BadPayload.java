package com.example.gqlapi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class BadPayload implements Error, MutationError {

    @NonNull
    private final String payload;

    @Override
    public String getMessage() {
        return "Payload is not a valid string representation of an integer";
    }
}
