# Tyk Issue Demo App

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
  myMutation(
    input: {
      status: "a"
    }
  ) {
    ...on MyMutationResult {
      status
    }
    ...on MyMutationErrors {
      errors {
        ...on UserError {
          __typename
        }
      }
    }
  }
}
```

For example, with `curl`:

```shell
curl --location --request POST 'http://localhost:8080/graphql' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"mutation {\n  myMutation(\n    input: {\n      status: \"a\"\n    }\n  ) {\n    ...on MyMutationResult {\n      status\n    }\n    ...on MyMutationErrors {\n      errors {\n        ...on UserError {\n          __typename\n        }\n      }\n    }\n  }\n}","variables":{}}'
```