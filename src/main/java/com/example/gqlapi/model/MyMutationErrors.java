package com.example.gqlapi.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMutationErrors implements MyMutationPayload {

    private final List<MyMutationError> errors;
}
