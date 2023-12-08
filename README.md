# 🏨 Hotel Rating Service 🏨

![CI/CD status](https://github.com/erickgualpa/hotel-rating-service/actions/workflows/maven.yml/badge.svg)

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

📣 This project has been structured following an Hexagonal Architecure
```
.
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── egualpam
    │   │           └── services
    │   │               └── hotel
    │   │                   └── rating
    │   │                       ├── application
    │   │                       │   ├── hotels
    │   │                       │   └── reviews
    │   │                       ├── domain
    │   │                       │   ├── hotels
    │   │                       │   ├── reviews
    │   │                       │   └── shared
    │   │                       └── infrastructure
    │   │                           ├── configuration
    │   │                           ├── controller
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
        │               └── hotel
        │                   └── rating
        │                       ├── application
        │                       │   ├── hotels
        │                       │   └── reviews
        │                       ├── e2e
        │                       ├── helpers
        │                       └── infrastructure
        │                           ├── controller
        │                           └── persistence
        │                               └── jpa
        └── resources
```
