package com.example.gqlapi.controller;

import java.time.Duration;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MySubscriptionController {

    @SubscriptionMapping
    public Flux<String> mySubscription() {
        log.info("mySubscription");
        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> LocalTime.now().toString());
    }
}
