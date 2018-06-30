package com.ge.rfr.common.exception;

/**
 * Thrown to indicate that an entity that was referenced in a requested by the client could not be found,
 * but is crucial to continuing with request processing.
 * Can (and should) be subclassed for individual entity types (such as projects, etc.) to make it easier to
 * build the correct messages.
 */
public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

}
