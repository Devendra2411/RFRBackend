package com.ge.rfr.workflow;

import com.ge.rfr.common.exception.EntityNotFoundException;

/**
 * Thrown when user tries to view or update a invalid workflow
 *
 */
public class RfrWorkflowNotFoundException extends EntityNotFoundException {

    public RfrWorkflowNotFoundException(int workflowId) {
        super("Could not find RFR Workflow with id: '" + workflowId + "'.");
    }
}