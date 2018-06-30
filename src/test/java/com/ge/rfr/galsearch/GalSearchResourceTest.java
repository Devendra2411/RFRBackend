package com.ge.rfr.galsearch;

import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.testng.annotations.Test;

import com.ge.rfr.galsearch.dto.EmployeeDetailsDto;
import com.ge.rfr.helper.AbstractIntegrationTest;

public class GalSearchResourceTest extends AbstractIntegrationTest {

	private final GalSearchClient client = new GalSearchClient(this::getClient);

	// Hard coding the SSO to test the Gal Search API
	// Change the SSO if in case this test fails 
	@Test
	public void testGetEmployeeDetails() throws Exception {

		List<EmployeeDetailsDto> employeesList = client.getEmployeeDetails("5030558");
		assertNotEquals(0, employeesList.size());
	}
}
