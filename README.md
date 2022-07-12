# Tyk Issue Demo App

## Requirements

* Java 17
* Docker
* Docker Compose

## Steps to reproduce

1. Build GraphQL backend and run it with Tyk and Redis: 

```shell
./gradlew build image && docker compose up
```

2. Open the following link in browser: [http://localhost:8080/graphql/playground](http://localhost:8080/graphql/playground) 

3. Execute the subscription in API Playground:

```
subscription {
  mySubscription
}
```
