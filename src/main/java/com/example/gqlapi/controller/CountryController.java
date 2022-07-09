package com.example.gqlapi.controller;

import com.example.gqlapi.model.Country;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CountryController {

    @SchemaMapping
    public String comment(Country country) {
        return "This is a sample comment for country " + country.getCode();
    }
}
