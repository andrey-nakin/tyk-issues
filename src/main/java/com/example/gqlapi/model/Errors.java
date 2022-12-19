package com.example.gqlapi.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Errors implements Payload {

    private final List<Error> errors;
}
