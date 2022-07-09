package com.example.gqlapi.config;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.example.gqlapi.model.Country;
import graphql.TypeResolutionEnvironment;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.RuntimeWiring;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {

    @Bean
    public GraphQlSourceBuilderCustomizer federationTransform() {
        return builder -> builder.schemaFactory((registry, wiring) -> Federation.transform(registry, wiring)
                .setFederation2(true)
                .fetchEntities(this::fetchEntities)
                .resolveEntityType(this::resolveEntityType)
                .build());
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return RuntimeWiring.Builder::build;
    }

    private Object fetchEntities(final DataFetchingEnvironment dataFetchingEnvironment) {
        return dataFetchingEnvironment.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                .stream()
                .map(values -> Country.builder().code((String) values.get("code")).build())
                .collect(Collectors.toList());
    }

    private GraphQLObjectType resolveEntityType(final TypeResolutionEnvironment environment) {
        return environment.getSchema().getObjectType("Country");
    }
}
