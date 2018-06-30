package com.ge.rfr.common.errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InvalidApiUriErrorDetails extends ErrorDetails {

    private final String message;

    @JsonCreator
    public InvalidApiUriErrorDetails(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
