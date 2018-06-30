package com.ge.rfr.helper;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class ModelValidator {

	private final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * Check that the expected count of ConstraintViolations are found for the
	 * entity class
	 */
	public void checkCountOfConstraintViolations(Object object, int expectedCount) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

		constraintViolations
				.forEach(violation -> System.out.println(violation.getPropertyPath() + ": " + violation.getMessage()));

		assertEquals(constraintViolations.size(), expectedCount);
	}

}
