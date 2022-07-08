package com.example.gqlapi.model;

public class EmptyArgumentError implements UserError, MyMutationError {

    @Override
    public String getPath() {
        return "arg";
    }
}
