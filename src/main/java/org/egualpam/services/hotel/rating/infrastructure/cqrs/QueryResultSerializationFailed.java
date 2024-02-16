package org.egualpam.services.hotel.rating.infrastructure.cqrs;

public class QueryResultSerializationFailed extends RuntimeException {
    public QueryResultSerializationFailed(Throwable cause) {
        super(cause);
    }
}
