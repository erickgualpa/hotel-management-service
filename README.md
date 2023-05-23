# Hotel Review Search and Sorting

### How to run hotel-rating-service:

Using wrapped maven included in this repository, with following commands:

- Compile:
  <br>

```shell script
./mvnw clean package
```

- Run service:
  <br>

```shell script
./mvnw clean spring-boot:run
```

- Example query request:
  <br>

```shell script
curl -X POST -H "Content-Type: application/json" -d '
{
    "location": "Barcelona",
    "checkIn": "2023-06-24",
    "checkOut": "2023-06-28",
    "priceRange": {
        "begin": 0,
        "end": 2000
    }
}' \
localhost:8080/api/hotel/query
```