package org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared;

import com.networknt.schema.ValidationMessage;
import java.util.Set;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.PublicEvent;

public class InvalidPublicEvent extends RuntimeException {
  public InvalidPublicEvent(PublicEvent publicEvent, Set<ValidationMessage> validationResult) {
    super(
        "Invalid public event: [%s] due to [%s]"
            .formatted(publicEvent.getClass().getSimpleName(), validationResult));
  }
}
