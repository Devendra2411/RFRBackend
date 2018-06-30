package com.ge.rfr.common.util;

import com.ge.rfr.common.exception.InValidDataSubmittedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ModelValidations {

    @Autowired
    private Validator validator;


    /**
     * Check if any ConstraintViolation is found in the entity class
     *
     * @param object
     * @throws InValidDataSubmittedException
     */
    public void checkConstraintViolation(Object object) throws InValidDataSubmittedException {
        Set<ConstraintViolation<Object>> constraintViolations =
                validator.validate(object);

        if (constraintViolations.size() > 0) {
            throw new InValidDataSubmittedException(constraintViolations.iterator().next().getMessage());
        }
    }

}
