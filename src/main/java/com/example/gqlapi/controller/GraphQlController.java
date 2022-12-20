package com.example.gqlapi.controller;

import com.example.gqlapi.model.IResponse;
import com.example.gqlapi.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphQlController {

    @QueryMapping
    public IResponse getResponse() {
        return Response.builder().result(123).build();
    }
}
