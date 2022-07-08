package com.example.gqlapi.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocaleSpecificationInput {

    private final String locale;
    private final PredefinedLocaleType predefinedLocale;
}
