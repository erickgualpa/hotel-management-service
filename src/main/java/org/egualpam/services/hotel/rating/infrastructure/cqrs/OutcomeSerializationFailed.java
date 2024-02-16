package org.egualpam.services.hotel.rating.infrastructure.cqrs;

public class OutcomeSerializationFailed extends RuntimeException {
    public OutcomeSerializationFailed(Throwable cause) {
        super(cause);
    }
}
