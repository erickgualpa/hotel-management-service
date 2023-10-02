# 🏨 Hotel Rating Service 🏨

![CI/CD status](https://github.com/erickgualpa/hotel-rating-service/actions/workflows/maven.yml/badge.svg)

Using wrapped maven included in this repository, you can:

🚀 Compile and Run service as container! 🐳
<br>

```shell script
./mvnw clean package
docker compose up -d
```

💤 Clear service containers

```shell script
docker compose down
docker rmi hotel-rating-service:latest
```

🔹 Use of this service is specified through [SpringDoc OpenAPI Swagger UI](http://localhost:8080/swagger-ui/index.html).
Check it and try to find the best available hotel in Mars! 👽
<br>
