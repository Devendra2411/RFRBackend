package com.ge.rfr.common.exception;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * Exception for unrecoverable application errors in RFR.
 */
public class RFRException extends RuntimeException {

    private final String message;

    public RFRException(String message) {
        this.message = message;
    }

    public RFRException(String message, Object... args) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, args);

        if (formattingTuple.getThrowable() != null) {
            initCause(formattingTuple.getThrowable());
        }

        this.message = formattingTuple.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
