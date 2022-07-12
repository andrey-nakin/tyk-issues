package com.example.gqlapi.controller;

import com.example.gqlapi.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MySubscriptionController {

    private final DataRepository repository;

    @SubscriptionMapping
    public Flux<String> mySubscription() {
        log.info("mySubscription");
        return this.repository.getStream();
    }
}
