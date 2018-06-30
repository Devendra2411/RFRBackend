package com.ge.rfr.actionitem;

import com.ge.rfr.common.exception.EntityNotFoundException;

/**
 * Thrown when the user tries to update action item with invalid action
 * item id
 */
public class RfrActionItemNotFoundException extends EntityNotFoundException {

    public RfrActionItemNotFoundException(int itemId) {
        super("Could not find RFR Action Item with id: '" + itemId + "'.");
    }
}