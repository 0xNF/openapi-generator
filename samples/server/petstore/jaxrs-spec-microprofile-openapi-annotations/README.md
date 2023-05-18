# JAX-RS server with OpenAPI using Quarkus

## Overview
This server was generated by the [OpenAPI Generator](https://openapi-generator.tech) project. By using an
[OpenAPI-Spec](https://openapis.org), you can easily generate a server stub.

This is an example of building a OpenAPI-enabled JAX-RS server.
This example uses the [JAX-RS](https://jax-rs-spec.java.net/) framework and
the [Eclipse-MicroProfile-OpenAPI](https://github.com/eclipse/microprofile-open-api) addition.

The pom file is configured to use [Quarkus](https://quarkus.io/) as application server.


To start the server in dev mode, run this maven command:

```bash
mvn compile quarkus:dev
```

You can then call your server endpoints under:

```
http://localhost:8080/
```

In dev-mode, you can open Swagger-UI at:

```
http://localhost:8080/swagger-ui/
```

Read more in the [Quarkus OpenAPI guide](https://quarkus.io/guides/openapi-swaggerui-guide).
