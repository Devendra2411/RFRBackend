package com.ge.rfr.workflow;

import static com.ge.rfr.helper.TestUser.RFR_ADMIN;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.rfr.fspevent.model.TrainDataDto;
import com.ge.rfr.helper.TestUser;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDetailsDto;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDto;
import com.ge.rfr.workflow.model.dto.SerialNumberDetailsDto;

public class RfrWorkflowClient {
	
	public final ClientSupplier clientSupplier;
	
	@FunctionalInterface
    public interface ClientSupplier {
        WebTarget getClient(TestUser user) throws Exception;
    }
	
	 public RfrWorkflowClient(ClientSupplier clientSupplier) {
	        this.clientSupplier = clientSupplier;
	 }

	 public WebTarget getClient(TestUser user) throws Exception {
	        return clientSupplier.getClient(user);
	 }
	
	public List<TrainDataDto> getBlocks(TestUser user,RfrWorkflowDetailsDto rfrWorkflowDetailsDto) throws Exception
	{
		return Arrays.asList(getClient(user).path("/rfr-workflow/getBlocks")
                .request()
                .post(Entity.json(rfrWorkflowDetailsDto),TrainDataDto[].class));
	}
	
	public List<SerialNumberDetailsDto>  getOutageId(String esnId) throws Exception
	{
		return Arrays.asList(getClient(RFR_ADMIN).path("/rfr-workflow/esn/{esnId}")
				.resolveTemplate("esnId", esnId)
                .request()
                .get(SerialNumberDetailsDto[].class));
	}
	
	public List<RfrWorkflowDto> createRfrWorkflow(TestUser user, List<RfrWorkflowDto> createDtoList) throws Exception {
		return Arrays.asList(getClient(user).path("/rfr-workflow")
							  .request()
							  .post(Entity.json(createDtoList),RfrWorkflowDto[].class));
	}
	
	public RfrWorkflowDto updateRfrWorkflow(TestUser user, int workflowId, RfrWorkflowDto rfrWorkflowDto)
			throws Exception {
		return getClient(user).path("/rfr-workflow/{workflowId}")
				              .resolveTemplate("workflowId", workflowId)
				              .request()
				              .put(Entity.json(rfrWorkflowDto), RfrWorkflowDto.class);
	} 

	public RfrWorkflowDto getRfrWorkflowDetails(int workflowId) throws Exception {
		return getClient(TestUser.RFR_ADMIN).path("/rfr-workflow/{workflowId}")
				                          .resolveTemplate("workflowId", workflowId)
				                          .request()
				                          .get(RfrWorkflowDto.class);
	}
	
	public List<RfrWorkflowDetailsDto> getRfrWorkflowList(TestUser user) throws Exception{
		return Arrays.asList(getClient(user)
				     .path("/rfr-workflow")
                     .request()
                     .get(RfrWorkflowDetailsDto[].class));
	}
	
	public List<RfrWorkflowDetailsDto> getSites(TestUser user) throws Exception
	{
		return Arrays.asList(getClient(user)
				     .path("/rfr-workflow/sites")
                     .request()
                     .get(RfrWorkflowDetailsDto[].class));
	}
	
	
	
}
