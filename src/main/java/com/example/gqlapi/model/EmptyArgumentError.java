package com.example.gqlapi.model;

import java.util.Collections;
import java.util.List;

public class EmptyArgumentError implements UserError, MyMutationError {

    @Override
    public List<String> getPath() {
        return Collections.singletonList("arg");
    }
}
