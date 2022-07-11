package com.example.gqlapi.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GetStringController {

    @QueryMapping
    public String getString() {
        return "Sample string";
    }
}
