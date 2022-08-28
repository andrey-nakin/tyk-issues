# Tyk Issue Demo App

This project demonstrates a bug in Tyk that occurs when Redis API is called from a middleware written in Python. 

## Requirements

* Docker
* Docker Compose

## Steps to reproduce

1. Build the middleware: 

### Unix

```shell
./gradlew build
```

### Windows

```shell
gradlew build
```

2. Run Tyk and its dependencies:

```shell
docker compose up
```

or

```shell
docker-compose up
```

3. Execute the subscription in API Playground:

```shell
curl --location --request POST 'http://localhost:8080/country-service/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"query { countries { code name }}","variables":{}}'
```

Notice error dump in Tyk logs.