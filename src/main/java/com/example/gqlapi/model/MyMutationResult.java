package com.example.gqlapi.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMutationResult implements MyMutationPayload {

    private final String status;
}
