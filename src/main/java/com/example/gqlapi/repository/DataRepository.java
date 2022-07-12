package com.example.gqlapi.repository;

import java.time.Duration;

import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class DataRepository {

    public Flux<String> getStream() {
        log.info("getStream");
        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> LocalTime.now().toString());
    }
}
