# 🏨 Hotel Management Service 🏨

![CI/CD status](https://github.com/erickgualpa/hotel-management-service/actions/workflows/maven.yml/badge.svg)

Using wrapped maven included in this repository, you can:

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
    │   │                   ├── application
    │   │                   │   ├── hotels
    │   │                   │   ├── reviews
    │   │                   │   └── shared
    │   │                   ├── domain
    │   │                   │   ├── hotels
    │   │                   │   │   └── exception
    │   │                   │   ├── reviews
    │   │                   │   │   └── exception
    │   │                   │   └── shared
    │   │                   │       └── exception
    │   │                   └── infrastructure
    │   │                       ├── configuration
    │   │                       ├── controller
    │   │                       ├── cqrs
    │   │                       │   └── simple
    │   │                       ├── events
    │   │                       │   └── publishers
    │   │                       │       └── simple
    │   │                       └── persistence
    │   │                           └── jpa
    │   └── resources
    │       └── db
    │           └── migration
    └── test
        ├── java
        │   └── org
        │       └── egualpam
        │           └── services
        │               └── hotelmanagement
        │                   ├── application
        │                   │   └── reviews
        │                   ├── e2e
        │                   ├── helpers
        │                   └── infrastructure
        │                       ├── configuration
        │                       ├── controller
        │                       └── persistence
        │                           └── jpa
        └── resources
```
