# Tyk Issue Demo App

**FIXED in v4.3.0**

## Requirements

* Java 17
* Docker
* Docker Compose

## Steps to reproduce

* Build GraphQL backend and run it with Tyk and Redis: 

```shell
./gradlew build image && docker compose up
```

* Send the following GraphQL request to endpoint `http://localhost:8080/graphql`:

```shell
{
  countries {
    code
    name
    comment
  }
}
```

For example, with `curl`:

```shell
curl --location --request POST 'http://localhost:8080/graphql' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"{\n  countries {\n    code\n    name\n    comment\n  }\n}","variables":{}}'
```