package com.ge.rfr.common.exception;

/**
 * This class is used for invalid file uploaded exception.
 *
 * @author 503055886
 */
public class FileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4794415318129531423L;

    public FileNotFoundException() {
        super("File not found!");
    }

    public FileNotFoundException(String message) {
        super(message);
    }

}
