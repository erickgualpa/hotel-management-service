# 🏨 Hotel Management Service 🏨

![CI/CD status](https://github.com/erickgualpa/hotel-management-service/actions/workflows/maven.yml/badge.svg)
[![](https://img.shields.io/badge/Spring%20Boot%20Version-3.4.1-blue)](/pom.xml)
[![](https://img.shields.io/badge/Java%20Version-21-blue)](/pom.xml)

🎮 This is a **playground** project that replicates simple features from a hotel management backend service.
<br>
🤔 Main goal of this is to achieve a flexible architecture considering popular concepts like DDD, Hexagonal Architecture
or
SOLID among others.

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
    │   │           └── contexts
    │   │               └── hotelmanagement
    │   │                   ├── hotel
    │   │                   │   ├── application
    │   │                   │   │   ├── command
    │   │                   │   │   └── query
    │   │                   │   ├── domain
    │   │                   │   └── infrastructure
    │   │                   │       ├── configuration
    │   │                   │       ├── consumer
    │   │                   │       ├── controller
    │   │                   │       ├── cqrs
    │   │                   │       │   ├── command
    │   │                   │       │   │   └── simple
    │   │                   │       │   └── query
    │   │                   │       │       └── simple
    │   │                   │       ├── readmodelsupplier
    │   │                   │       │   └── jpa
    │   │                   │       ├── repository
    │   │                   │       │   └── jpa
    │   │                   │       ├── reviewisalreadyprocessed
    │   │                   │       └── shared
    │   │                   │           └── jpa
    │   │                   │               └── hotelaveragerating
    │   │                   ├── review
    │   │                   │   ├── application
    │   │                   │   │   ├── command
    │   │                   │   │   └── query
    │   │                   │   ├── domain
    │   │                   │   └── infrastructure
    │   │                   │       ├── configuration
    │   │                   │       ├── controller
    │   │                   │       ├── cqrs
    │   │                   │       │   ├── command
    │   │                   │       │   │   └── simple
    │   │                   │       │   └── query
    │   │                   │       │       └── simple
    │   │                   │       ├── readmodelsupplier
    │   │                   │       └── repository
    │   │                   └── shared
    │   │                       ├── application
    │   │                       │   ├── command
    │   │                       │   └── query
    │   │                       ├── domain
    │   │                       └── infrastructure
    │   │                           ├── configuration
    │   │                           │   └── eventbus
    │   │                           ├── cqrs
    │   │                           │   ├── command
    │   │                           │   │   └── simple
    │   │                           │   └── query
    │   │                           │       └── simple
    │   │                           ├── eventbus
    │   │                           │   ├── events
    │   │                           │   ├── shared
    │   │                           │   ├── simple
    │   │                           │   └── springamqp
    │   │                           └── internaleventbus
    │   │                               └── spring
    │   └── resources
    │       └── db
    │           └── migration
    └── test
        ├── java
        │   └── org
        │       └── egualpam
        │           └── contexts
        │               └── hotelmanagement
        │                   ├── architecture
        │                   ├── e2e
        │                   ├── hotel
        │                   │   ├── application
        │                   │   │   ├── command
        │                   │   │   └── query
        │                   │   └── infrastructure
        │                   │       ├── controller
        │                   │       ├── readmodelsupplier
        │                   │       │   └── jpa
        │                   │       ├── repository
        │                   │       │   └── jpa
        │                   │       └── reviewisalreadyprocessed
        │                   ├── journey
        │                   ├── review
        │                   │   ├── application
        │                   │   │   ├── command
        │                   │   │   └── query
        │                   │   └── infrastructure
        │                   │       ├── controller
        │                   │       ├── readmodelsupplier
        │                   │       └── repository
        │                   └── shared
        │                       └── infrastructure
        │                           ├── configuration
        │                           ├── cqrs
        │                           │   ├── command
        │                           │   │   └── simple
        │                           │   └── query
        │                           │       └── simple
        │                           ├── eventbus
        │                           │   ├── simple
        │                           │   └── springamqp
        │                           └── helpers
        └── resources
```
