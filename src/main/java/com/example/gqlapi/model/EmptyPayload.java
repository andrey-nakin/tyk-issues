package com.example.gqlapi.model;

public class EmptyPayload implements Error, MutationError {

    @Override
    public String getMessage() {
        return "Payload is empty";
    }
}
