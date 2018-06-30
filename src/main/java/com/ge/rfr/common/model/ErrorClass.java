package com.ge.rfr.common.model;

/**
 * This class is used for error class.
 *
 * @author 503055886
 */
public class ErrorClass {

    private String error;
    private String error_description;

    // Constructors
    public ErrorClass() {
    }

    public ErrorClass(String error, String error_description) {
        this.error = error;
        this.error_description = error_description;
    }

    // Getters and Setters

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    @Override
    public String toString() {
        return String.format("ErrorClass[error='%s', error_description=%s]", error, error_description);
    }

}
