package com.ge.rfr.common.exception;

/**
 * This class is used for invalid file uploaded exception.
 *
 * @author 503055886
 */
public class InValidFileUploadedException extends RuntimeException {

    private static final long serialVersionUID = 4794415318129531423L;

    public InValidFileUploadedException() {
        super("Invalid File.");
    }

    public InValidFileUploadedException(String message) {
        super(message);
    }

}
