# 🏨 Hotel Rating Service 🏨

![CI/CD status](https://github.com/erickgualpa/hotel-rating-service/actions/workflows/maven.yml/badge.svg)

Using wrapped maven included in this repository, you can:

🔨 Compile
  <br>

```shell script
./mvnw clean package
```

🚀 Run service
  <br>

```shell script
docker compose up -d
./mvnw clean spring-boot:run
```

🔹 Example query request
  <br>

```shell script
curl -X POST -H "Content-Type: application/json" -d '
{
    "location": "Barcelona",
    "priceRange": {
        "begin": 0,
        "end": 2000
    }
}' \
  localhost:8080/v1/hotels/query

```
