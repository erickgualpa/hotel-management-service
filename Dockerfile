FROM bellsoft/liberica-openjdk-alpine-musl:17
LABEL org.opencontainers.image.authors='egualpam'
COPY target/hotel-rating-service-0.0.1-SNAPSHOT.jar hotel-rating-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "hotel-rating-service-0.0.1-SNAPSHOT.jar"]