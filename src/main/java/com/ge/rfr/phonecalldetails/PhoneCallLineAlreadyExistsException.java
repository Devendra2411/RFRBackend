package com.ge.rfr.phonecalldetails;

import com.ge.rfr.common.exception.UniqueConstraintException;

/**
 * Thrown when the name used for a new or changed phone call meeting
 * is already in use by another phone call meeting
 */
public class PhoneCallLineAlreadyExistsException extends UniqueConstraintException {

    public PhoneCallLineAlreadyExistsException() {
        super("meetingLine");

    }

}
