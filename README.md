# Tyk Issue Demo App

This project demonstrates a bug in Tyk that occurs when an extra field is added to input variable.

**FIXED in v4.3.0.**

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

input StringQueryOperatorInput {
    eq: String
    ne: String
    in: [String]
    nin: [String]
    regex: String
    glob: String
}

input CountryFilterInput {
    code: StringQueryOperatorInput
    currency: StringQueryOperatorInput
    continent: StringQueryOperatorInput
}

type Query {
    countries(filter: CountryFilterInput): [Country!]!
}
```

Then I'm trying to run a query with a minor error: an extra field is added to the input parameters:

Query:

```graphql
query MyQuery($filter: CountryFilterInput) {
    countries(filter: $filter) {
        code
        name
    }
}
```

and values:

```json
{
  "filter": {
    "code": {
      "in": [
        "US"
      ],
      "extra": "value"
    }
  }
}
```

Please notice `extra` input field in the variables.

When I run the query above, Tyk fails with a panic error. Here's a piece of Tyk log:

```
tyk-issues-tyk-gateway-1  | 2022/09/06 18:35:13 http: panic serving 172.28.0.1:36284: runtime error: index out of range [-1]
tyk-issues-tyk-gateway-1  | goroutine 2925 [running]:
tyk-issues-tyk-gateway-1  | net/http.(*conn).serve.func1(0xc00033ce60)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:1801 +0x147
tyk-issues-tyk-gateway-1  | panic(0x23ce360, 0xc00016a560)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/runtime/panic.go:975 +0x47a
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/ast.(*Document).InputValueDefinitionType(...)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/ast/ast_input_value_definition.go:52
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*inputCoercionForListVisitor).walkJsonObject.func1(0xc00012c97b, 0x5, 0x19, 0xc00012c984, 0x5, 0x5, 0x1, 0x38, 0x0, 0x0)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/input_coercion_for_list.go:195 +0x2dd
tyk-issues-tyk-gateway-1  | github.com/buger/jsonparser.ObjectEach(0xc00012c952, 0x42, 0x42, 0xc00072caf0, 0x0, 0x0, 0x0, 0xe21efc68fce2dede, 0xc00072cb08)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/buger/jsonparser/parser.go:1128 +0x41a
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*inputCoercionForListVisitor).walkJsonObject(0xc0005188c0, 0x0, 0xc00012c952, 0x42, 0x42)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/input_coercion_for_list.go:190 +0x9b
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*inputCoercionForListVisitor).processTypeKindNamed(0xc0005188c0, 0xc000943430, 0xe, 0xc00012c952, 0x42, 0x42, 0x3)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/input_coercion_for_list.go:258 +0xeb
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*inputCoercionForListVisitor).walkJsonObject.func1(0xc00012c94b, 0x4, 0x4f, 0xc00012c952, 0x42, 0x42, 0x3, 0x54, 0x0, 0x0)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/input_coercion_for_list.go:203 +0x23e
tyk-issues-tyk-gateway-1  | github.com/buger/jsonparser.ObjectEach(0xc00012c940, 0x5a, 0x5a, 0xc00072cd08, 0x0, 0x0, 0x0, 0x147090bd5e48cd5c, 0xc00072cd20)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/buger/jsonparser/parser.go:1128 +0x41a
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*inputCoercionForListVisitor).walkJsonObject(0xc0005188c0, 0x1, 0xc00012c940, 0x5a, 0x5a)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/input_coercion_for_list.go:190 +0x9b
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*inputCoercionForListVisitor).processTypeKindNamed(0xc0005188c0, 0xc000640038, 0x0, 0xc00012c940, 0x5a, 0x5a, 0x3)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/input_coercion_for_list.go:258 +0xeb
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*inputCoercionForListVisitor).EnterVariableDefinition(0xc0005188c0, 0x0)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/input_coercion_for_list.go:69 +0x4a5
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astvisitor.(*Walker).walkVariableDefinition(0xc00053ea00, 0x0)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astvisitor/visitor.go:1623 +0xf6
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astvisitor.(*Walker).walkOperationDefinition(0xc00053ea00, 0x0)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astvisitor/visitor.go:1571 +0x225
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astvisitor.(*Walker).walk(0xc00053ea00)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astvisitor/visitor.go:1475 +0x5f5
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astvisitor.(*Walker).Walk(0xc00053ea00, 0xc000640038, 0xc000943430, 0xc00021f560)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astvisitor/visitor.go:1301 +0xa5
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/astnormalization.(*OperationNormalizer).NormalizeOperation(0xc00021f680, 0xc000640038, 0xc000943430, 0xc00021f560)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/astnormalization/astnormalization.go:206 +0xd6
tyk-issues-tyk-gateway-1  | github.com/jensneuse/graphql-go-tools/pkg/graphql.(*Request).Normalize(0xc000640000, 0xc000943400, 0xc000640000, 0x0, 0x0, 0xc0007a44e0, 0xc00016a300)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/jensneuse/graphql-go-tools/pkg/graphql/normalization.go:34 +0x3d4
tyk-issues-tyk-gateway-1  | github.com/TykTechnologies/tyk/gateway.(*GraphQLMiddleware).ProcessRequest(0xc000933620, 0x282db40, 0xc00022e8c0, 0xc00055a200, 0x0, 0x0, 0x0, 0x0, 0x0)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/TykTechnologies/tyk/gateway/mw_graphql.go:232 +0x745
tyk-issues-tyk-gateway-1  | github.com/TykTechnologies/tyk/gateway.TraceMiddleware.ProcessRequest(0x2845700, 0xc000933620, 0x282db40, 0xc00022e8c0, 0xc00055a200, 0x0, 0x0, 0x0, 0x0, 0x0)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/TykTechnologies/tyk/gateway/middleware.go:65 +0x293
tyk-issues-tyk-gateway-1  | github.com/TykTechnologies/tyk/gateway.(*Gateway).createMiddleware.func1.1(0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/TykTechnologies/tyk/gateway/middleware.go:130 +0x55f
tyk-issues-tyk-gateway-1  | net/http.HandlerFunc.ServeHTTP(0xc0002eac80, 0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:2042 +0x44
tyk-issues-tyk-gateway-1  | github.com/TykTechnologies/tyk/gateway.(*Gateway).createMiddleware.func1.1(0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/TykTechnologies/tyk/gateway/middleware.go:165 +0xf9c
tyk-issues-tyk-gateway-1  | net/http.HandlerFunc.ServeHTTP(0xc0002eacd0, 0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:2042 +0x44
tyk-issues-tyk-gateway-1  | github.com/TykTechnologies/tyk/gateway.(*Gateway).createMiddleware.func1.1(0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/TykTechnologies/tyk/gateway/middleware.go:165 +0xf9c
tyk-issues-tyk-gateway-1  | net/http.HandlerFunc.ServeHTTP(0xc0002ead20, 0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:2042 +0x44
tyk-issues-tyk-gateway-1  | github.com/rs/cors.(*Cors).Handler.func1(0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/rs/cors/cors.go:219 +0x1b9
tyk-issues-tyk-gateway-1  | net/http.HandlerFunc.ServeHTTP(0xc0006c8160, 0x282db40, 0xc00022e8c0, 0xc00055a200)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:2042 +0x44
tyk-issues-tyk-gateway-1  | github.com/gorilla/mux.(*Router).ServeHTTP(0xc000206b40, 0x282db40, 0xc00022e8c0, 0xc000130600)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/gorilla/mux/mux.go:210 +0xd3
tyk-issues-tyk-gateway-1  | github.com/TykTechnologies/tyk/gateway.(*handleWrapper).ServeHTTP(0xc000120248, 0x282db40, 0xc00022e8c0, 0xc000130600)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/TykTechnologies/tyk/gateway/proxy_muxer.go:54 +0x156
tyk-issues-tyk-gateway-1  | golang.org/x/net/http2/h2c.h2cHandler.ServeHTTP(0x27ff740, 0xc000120248, 0xc0000a4900, 0x282db40, 0xc00022e8c0, 0xc000130600)
tyk-issues-tyk-gateway-1  | 	/go/src/golang.org/x/net/http2/h2c/h2c.go:104 +0x437
tyk-issues-tyk-gateway-1  | github.com/TykTechnologies/tyk/gateway.(*h2cWrapper).ServeHTTP(0xc0003b5be0, 0x282db40, 0xc00022e8c0, 0xc000130600)
tyk-issues-tyk-gateway-1  | 	/go/src/github.com/TykTechnologies/tyk/gateway/proxy_muxer.go:42 +0x52
tyk-issues-tyk-gateway-1  | net/http.serverHandler.ServeHTTP(0xc00022eb60, 0x282db40, 0xc00022e8c0, 0xc000130600)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:2843 +0xa3
tyk-issues-tyk-gateway-1  | net/http.(*conn).serve(0xc00033ce60, 0x28348c0, 0xc00097e800)
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:1925 +0x8ad
tyk-issues-tyk-gateway-1  | created by net/http.(*Server).Serve
tyk-issues-tyk-gateway-1  | 	/usr/local/go/src/net/http/server.go:2969 +0x36c
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

2. Execute the following GraphQL query. Notice that we're passing an `extra` input field that is not presented in
   GraphQL schema.

```shell
curl --location --request POST 'http://localhost:8080/country-service/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"query MyQuery($filter: CountryFilterInput) {countries(filter: $filter) { code name } }","variables":{"filter":{"code":{"in":["US"],"extra":"value"}}}}'
```

Notice error dump in Tyk logs.

3. Now execute the GraphQL query below. Here we removed the bad input field and query works.

```shell
curl --location --request POST 'http://localhost:8080/country-service/' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"query MyQuery($filter: CountryFilterInput) {countries(filter: $filter) { code name } }","variables":{"filter":{"code":{"in":["US"]}}}}'
```
