package com.ge.rfr.actionitem;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import com.ge.rfr.actionitem.model.dto.RfrActionItemDetailsDto;
import com.ge.rfr.actionitem.model.dto.RfrActionItemDto;
import com.ge.rfr.helper.TestUser;

public class RfrActionItemClient {
	
public final ClientSupplier clientSupplier;
	
	@FunctionalInterface
    public interface ClientSupplier {
        WebTarget getClient(TestUser user) throws Exception;
    }
	
	 public RfrActionItemClient(ClientSupplier clientSupplier) {
	        this.clientSupplier = clientSupplier;
	 }

	 public WebTarget getClient(TestUser user) throws Exception {
	        return clientSupplier.getClient(user);
	 }
	 
	public RfrActionItemDetailsDto getRfrActionItemsList(TestUser user, int workflowId) throws Exception {
		return getClient(user).path("/rfr-workflow/{workflowId}/action-items")
						.resolveTemplate("workflowId", workflowId)
						.request().get(RfrActionItemDetailsDto.class);
	}
	
	public RfrActionItemDto createRfrActionItem(TestUser user, int workflowId, 
											    RfrActionItemDto createActionItemDto) throws Exception {
		return getClient(user).path("/rfr-workflow/{workflowId}/action-items")
				.resolveTemplate("workflowId", workflowId).request()
				.post(Entity.json(createActionItemDto), RfrActionItemDto.class);
	}
	
	public RfrActionItemDto updateRfrActionItem(TestUser user,int workflowId, int actionItemId, 
												RfrActionItemDto updateActionItemDto) throws Exception {
		return getClient(user).path("/rfr-workflow/{workflowId}/action-items/{actionItemId}")
							  .resolveTemplate("workflowId", workflowId)
							  .resolveTemplate("actionItemId", actionItemId)
							  .request()
				              .put(Entity.json(updateActionItemDto), RfrActionItemDto.class);
	}
}
