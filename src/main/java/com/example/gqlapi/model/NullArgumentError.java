package com.example.gqlapi.model;

public class NullArgumentError implements UserError, MyMutationError {

    @Override
    public String getPath() {
        return "arg";
    }
}
