package com.example.gqlapi.controller;

import com.example.gqlapi.model.EmptyArgumentError;
import com.example.gqlapi.model.MyMutationErrors;
import com.example.gqlapi.model.MyMutationInput;
import com.example.gqlapi.model.MyMutationPayload;
import com.example.gqlapi.model.MyMutationResult;
import com.example.gqlapi.model.NullArgumentError;
import java.util.Collections;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MyMutationController {

    @MutationMapping
    public MyMutationPayload myMutation(@Argument MyMutationInput input) {
        if (input != null && input.getStatus() != null && input.getStatus().length() > 0) {
            return MyMutationResult.builder().status(input.getStatus()).build();
        } else if (input == null || input.getStatus() == null) {
            return MyMutationErrors.builder().errors(Collections.singletonList(new NullArgumentError())).build();
        } else {
            return MyMutationErrors.builder().errors(Collections.singletonList(new EmptyArgumentError())).build();
        }
    }
}
