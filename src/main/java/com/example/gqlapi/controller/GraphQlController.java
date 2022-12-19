package com.example.gqlapi.controller;

import com.example.gqlapi.model.BadPayload;
import com.example.gqlapi.model.EmptyPayload;
import com.example.gqlapi.model.Errors;
import com.example.gqlapi.model.Payload;
import com.example.gqlapi.model.Response;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphQlController {

    @QueryMapping
    public String getString() {
        return "sample string";
    }

    @MutationMapping
    public Payload parseString(@Argument @NonNull String payload) {
        if (StringUtils.isBlank(payload)) {
            return Errors.builder().errors(List.of(new EmptyPayload())).build();
        }

        try {
            return Response.builder().result(Integer.valueOf(payload)).build();
        } catch (NumberFormatException e) {
            return Errors.builder().errors(List.of(BadPayload.builder().payload(payload).build())).build();
        }
    }
}
