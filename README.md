# Tyk Issue Demo App

This project demonstrates an error when working with GraphQL interfaces.

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
query {
    getResponse {
        ...on Response {
            __typename
            result
        }
        ...on IResponse {
            __typename
            result
        }
    }
}
```

For example, with `curl`:

```shell
curl --location --request POST 'http://localhost:8080/graphql/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"query {\n    getResponse {\n        ...on Response {\n            __typename\n            result\n        }\n        ...on IResponse {\n            __typename\n            result\n        }\n    }\n}","variables":{}}'
```

The response is incorrect:

```
{
    "data": {
        "getResponse": {
            "__typename": "Response",
            "result": 123,
            "__typename": "Response",
            "result": 123
        }
    }
}
```
