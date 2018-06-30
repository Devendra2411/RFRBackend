package com.ge.rfr.phonecalldetails;

import com.ge.rfr.common.exception.EntityNotFoundException;

/**
 * Thrown when the user tries to view or update the phone call details
 * with invalid id
 */
public class PhoneCallMeetingNotFoundException extends EntityNotFoundException {

    public PhoneCallMeetingNotFoundException(int meetingId) {

        super("Could not find Phone Call Meeting with id: '" + meetingId + "'.");

    }

}


