package com.ge.rfr.common.exception;

/**
 * This class is used for invalid json exception.
 *
 * @author 503055886
 */
public class InvalidJsonException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidJsonException(String msg) {
		super(msg);
	}

}
