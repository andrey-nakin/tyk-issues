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
mutation {
  parseString(payload: "a1234") {
    ...on Response {
	    result
    }
    ...on Errors {
        errors {
            ...on EmptyPayload {
                __typename
                message
            }
            ...on BadPayload {
                __typename
                message
                payload
            }
            ...on MutationError {
                __typename
                message
            }
        }
    }
  }
}
```

For example, with `curl`:

```shell
curl --location --request POST 'http://localhost:8080/graphql/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"mutation {\n  parseString(payload: \"a1234\") {\n    ...on Response {\n\t    result\n    }\n    ...on Errors {\n        errors {\n            ...on EmptyPayload {\n                __typename\n                message\n            }\n            ...on BadPayload {\n                __typename\n                message\n                payload\n            }\n            ...on MutationError {\n                __typename\n                message\n            }\n        }\n    }\n  }\n}","variables":{}}'
```

The response is correct:

```
{
    "data": {
        "parseString": {
            "errors": [
                {
                    "__typename": "BadPayload",
                    "message": "Payload is not a valid string representation of an integer",
                    "payload": "a1234"
                }
            ]
        }
    }
}
```

* Now send the following GraphQL request:

```
mutation {
  parseString(payload: "a1234") {
    ...on Response {
	    result
    }
    ...on Errors {
        errors {
            ...on EmptyPayload {
                __typename
                message
            }
            ...on MutationError {
                __typename
                message
            }
        }
    }
  }
}
```

For example, with `curl`:

```shell
curl --location --request POST 'http://localhost:8080/graphql/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"mutation {\n  parseString(payload: \"a1234\") {\n    ...on Response {\n\t    result\n    }\n    ...on Errors {\n        errors {\n            ...on EmptyPayload {\n                __typename\n                message\n            }\n            ...on MutationError {\n                __typename\n                message\n            }\n        }\n    }\n  }\n}","variables":{}}'
```

The response is now invalid:

```
{
    "data": {
        "parseString": {
            "errors": [
                null
            ]
        }
    }
}
```

* Now send the following GraphQL request:

```
mutation {
  parseString(payload: "a1234") {
    ...on Response {
	    result
    }
    ...on Errors {
        errors {
            ...on MutationError {
                __typename
                message
            }
        }
    }
  }
}
```

For example, with `curl`:

```shell
curl --location --request POST 'http://localhost:8080/graphql/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"mutation {\n  parseString(payload: \"a1234\") {\n    ...on Response {\n\t    result\n    }\n    ...on Errors {\n        errors {\n            ...on MutationError {\n                __typename\n                message\n            }\n        }\n    }\n  }\n}","variables":{}}'
```

Now Tyk does not response at all due to internal failure and stack trace is displayed in Tyk logs.
