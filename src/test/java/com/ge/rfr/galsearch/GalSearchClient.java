package com.ge.rfr.galsearch;

import static com.ge.rfr.helper.TestUser.RFR_ADMIN;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.WebTarget;

import com.ge.rfr.galsearch.dto.EmployeeDetailsDto;
import com.ge.rfr.helper.TestUser;

public class GalSearchClient {

	public final ClientSupplier clientSupplier;

	@FunctionalInterface
	public interface ClientSupplier {
		WebTarget getClient(TestUser user) throws Exception;
	}

	public GalSearchClient(ClientSupplier clientSupplier) {
		this.clientSupplier = clientSupplier;
	}

	public WebTarget getClient(TestUser user) throws Exception {
		return clientSupplier.getClient(user);
	}

	public List<EmployeeDetailsDto> getEmployeeDetails(String searchText) throws Exception {
		return Arrays
				.asList(getClient(RFR_ADMIN).path("/galSearch/{searchText}")
						.resolveTemplate("searchText", searchText)
						.request().get(EmployeeDetailsDto[].class));
	}
}
