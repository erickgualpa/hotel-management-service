# 🏨 Hotel Management Service 🏨

![CI/CD status](https://github.com/erickgualpa/hotel-management-service/actions/workflows/maven.yml/badge.svg)
[![](https://img.shields.io/badge/Spring%20Boot%20Version-3.1.4-blue)](/pom.xml)
[![](https://img.shields.io/badge/Java%20Version-17-blue)](/pom.xml)

🎮 This is a **playground** project that replicates simple features from a hotel management backend service.
<br>
🤔 Main goal of this is achieve a flexible architecture considering popular concepts like DDD, Hexagonal Architecture or SOLID among others.

#### Using wrapped maven included in this repository, you can:

🧪 Run tests
<br>

```shell script
./mvnw clean verify
```

🚀 Build and deploy service as container! 🐳
<br>

```shell script
./build_and_deploy.sh
```

💤 Clear service containers

```shell script
docker compose down --rmi local
```

🔹 Use of this service is specified through [SpringDoc OpenAPI Swagger UI](http://localhost:8080/swagger-ui/index.html).
Check it and try to find the best available hotel in Mars! 👽
<br>

📣 This project has been structured following a Hexagonal Architecture

[//]: # (Directory tree below was generated using 'tree -d -I target' command)

```
.
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── egualpam
    │   │           └── services
    │   │               └── hotelmanagement
    │   │                   ├── hotels
    │   │                   │   ├── application
    │   │                   │   │   └── query
    │   │                   │   ├── domain
    │   │                   │   │   └── exception
    │   │                   │   └── infrastructure
    │   │                   │       ├── configuration
    │   │                   │       ├── controller
    │   │                   │       ├── cqrs
    │   │                   │       │   └── query
    │   │                   │       │       └── simple
    │   │                   │       └── persistence
    │   │                   │           └── jpa
    │   │                   ├── reviews
    │   │                   │   ├── application
    │   │                   │   │   ├── command
    │   │                   │   │   └── query
    │   │                   │   ├── domain
    │   │                   │   │   └── exception
    │   │                   │   └── infrastructure
    │   │                   │       ├── configuration
    │   │                   │       ├── controller
    │   │                   │       ├── cqrs
    │   │                   │       │   ├── command
    │   │                   │       │   │   └── simple
    │   │                   │       │   └── query
    │   │                   │       │       └── simple
    │   │                   │       └── persistence
    │   │                   │           └── jpa
    │   │                   └── shared
    │   │                       ├── application
    │   │                       │   ├── command
    │   │                       │   └── query
    │   │                       ├── domain
    │   │                       │   └── exception
    │   │                       └── infrastructure
    │   │                           ├── configuration
    │   │                           │   └── properties
    │   │                           │       └── eventbus
    │   │                           ├── cqrs
    │   │                           │   ├── command
    │   │                           │   │   └── simple
    │   │                           │   └── query
    │   │                           │       └── simple
    │   │                           ├── eventbus
    │   │                           │   ├── rabbitmq
    │   │                           │   └── simple
    │   │                           └── persistence
    │   │                               └── jpa
    │   └── resources
    │       └── db
    │           └── migration
    └── test
        ├── java
        │   └── org
        │       └── egualpam
        │           └── services
        │               └── hotelmanagement
        │                   ├── e2e
        │                   │   └── models
        │                   ├── hotels
        │                   │   └── infrastructure
        │                   │       ├── controller
        │                   │       ├── cqrs
        │                   │       │   └── query
        │                   │       │       └── simple
        │                   │       └── persistence
        │                   │           └── jpa
        │                   ├── reviews
        │                   │   ├── application
        │                   │   │   └── command
        │                   │   └── infrastructure
        │                   │       ├── controller
        │                   │       └── persistence
        │                   │           └── jpa
        │                   └── shared
        │                       └── infrastructure
        │                           ├── configuration
        │                           ├── cqrs
        │                           │   ├── command
        │                           │   │   └── simple
        │                           │   └── query
        │                           │       └── simple
        │                           └── helpers
        └── resources
```
