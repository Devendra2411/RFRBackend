package com.ge.rfr.phonecalldetails;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import com.ge.rfr.helper.TestUser;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDetailsDto;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDto;

public class PhoneCallDetailsClient {
	
public final ClientSupplier clientSupplier;
	
	@FunctionalInterface
    public interface ClientSupplier {
        WebTarget getClient(TestUser user) throws Exception;
    }
	
	 public PhoneCallDetailsClient(ClientSupplier clientSupplier) {
	        this.clientSupplier = clientSupplier;
	 }

	 public WebTarget getClient(TestUser user) throws Exception {
	        return clientSupplier.getClient(user);
	 }
	 
	 public PhoneCallDto getPhoneCallItems(TestUser user,int workflowId) throws Exception
		{
			return getClient(user).path("{workflowId}/phone-call")
						 .resolveTemplate("workflowId", workflowId)
		                 .request()
		                 .get(PhoneCallDto.class);
			
		}
		
		public PhoneCallDto getPhoneCallDetails(TestUser user,int workflowId,int meetingId) throws Exception {
			return getClient(user).path("{workflowId}/phone-call/minutes")
											  .resolveTemplate("workflowId", workflowId)
											  .queryParam("meetingId",meetingId)
					                          .request()
					                          .get(PhoneCallDto.class);
		}
		
	 public PhoneCallDetailsDto createPhoneCall(TestUser user,
			 									int workflowId,
			 									PhoneCallDetailsDto createDto) throws Exception
	 {
		  return getClient(user).path("{workflowId}/phone-call")
				  .resolveTemplate("workflowId", workflowId)
				  .request()
				  .post(Entity.json(createDto), PhoneCallDetailsDto.class); 
	 }

	public PhoneCallDetailsDto updatePhoneCall(TestUser user,
											   int workflowId,
											   int meetingId,
											   PhoneCallDetailsDto updateDto) throws Exception {
		
		return getClient(user).path("{workflowId}/phone-call")
				  .resolveTemplate("workflowId", workflowId)
				  .queryParam("meetingId",meetingId)
	              .request()
	              .put(Entity.json(updateDto), PhoneCallDetailsDto.class);
	}
	
}
