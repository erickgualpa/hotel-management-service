package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.View;

@FunctionalInterface
public interface QueryHandler {
  View handle(Query query);
}
