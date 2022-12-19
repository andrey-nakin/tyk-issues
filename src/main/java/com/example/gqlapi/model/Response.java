package com.example.gqlapi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Response implements Payload {

    @NonNull
    private final Integer result;
}
