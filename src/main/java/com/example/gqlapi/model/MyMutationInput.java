package com.example.gqlapi.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMutationInput {

    private final String status;
}
