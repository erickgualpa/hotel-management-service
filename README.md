# 🏨 Hotel Rating Service 🏨

![CI/CD status](https://github.com/erickgualpa/hotel-rating-service/actions/workflows/maven.yml/badge.svg)

Using wrapped maven included in this repository, you can:

🚀 Compile and Run service as container! 🐳
<br>

```shell script
./mvnw clean package
docker compose up -d
```

💤 Stop service containers

```shell script
docker compose down
docker rmi hotel-rating-service:latest
```

🔹 Example query request
<br>

```shell script
curl -X POST -H "Content-Type: application/json" -d '
{
    "location": "Mars",
    "priceRange": {
        "begin": 50000,
        "end": 150000
    }
}' \
  localhost:8080/v1/hotels/query
```
