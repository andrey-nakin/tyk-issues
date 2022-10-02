# Tyk Issue Demo App

This project demonstrates a bug in Tyk that occurs when an type introspection is requested.

## Requirements

* Docker
* Docker Compose

## Error description

I have the following GraphQL schema (I simply used a schema from the
public [Country API](https://countries.trevorblades.com/)):

```graphql
type Country {
    code: ID!
    name: String!
}

type Query {
   countries(filter: CountryFilterInput): [Country!]!
}

# other definitions are omitted here
```

Then I'm trying to run a query with a type introspection:

A query:

```graphql
query {
   __type(name: "Country") {
      name
   }
}
```

I expect a `Country` type description, but the whole schema is returned instead:

Response:

```json
{
    "data": {
        "__schema": {
            "queryType": {
                "name": "Query"

    // other fields are omitted here            
```

There's a workaround. If I add an arbitrary additional field to the query, it returns the expected type descriptor. 

A fixed query:

```graphql
query {
   __type(name: "Country") {
      name
   }
   countries(filter: { code: { in: "US" }}) {
      name
   }
}
```

and the response:

```json
{
    "data": {
        "__type": {
            "name": "Country"
        },
        "countries": [
            {
                "name": "United States"
            }
        ]
    }
}
```

## Steps to reproduce

1. Run Tyk and its dependencies:

```shell
docker compose up
```

or

```shell
docker-compose up
```

2. Execute the following GraphQL query.

```shell
curl --location --request POST 'http://localhost:8080/country-service/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"query {\n    __type(name: \"Country\") {\n        name\n    }\n}\n","variables":{}}'
```

Notice the bad response.

3. Now execute the fixed GraphQL query.

```shell
curl --location --request POST 'http://localhost:8080/country-service/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"query {\n    __type(name: \"Country\") {\n        name\n    }\n    countries(filter: { code: { in: \"US\" }}) {\n        name\n    }\n}\n","variables":{}}'
```

Notice the response with a valid type descriptor.

## Reproducibility

The issue exists in Tyk Gateway v4.1.0 and v4.2.0.