package com.ge.rfr.workflow;

import com.ge.rfr.common.exception.UniqueConstraintException;

/**
 * Thrown when the name used for a new or changed project is already in use by another project.
 */
public class WorkflowAlreadyExistsException extends UniqueConstraintException {

    public WorkflowAlreadyExistsException() {
        super("outageId");
    }

}