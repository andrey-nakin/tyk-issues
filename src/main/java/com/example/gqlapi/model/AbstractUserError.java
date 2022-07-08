package com.example.gqlapi.model;

import java.util.Collections;
import java.util.List;

public abstract class AbstractUserError implements UserError {

    @Override
    public List<String> getPath() {
        return Collections.singletonList("arg");
    }
}
