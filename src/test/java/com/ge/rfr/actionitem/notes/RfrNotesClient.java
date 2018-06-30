package com.ge.rfr.actionitem.notes;

import static com.ge.rfr.helper.TestUser.RFR_ADMIN;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import com.ge.rfr.actionitem.notes.model.dto.RfrNotesDto;
import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.helper.TestUser;

public class RfrNotesClient {
	
public final ClientSupplier clientSupplier;
	
	@FunctionalInterface
    public interface ClientSupplier {
        WebTarget getClient(TestUser user) throws Exception;
    }
	
	 public RfrNotesClient(ClientSupplier clientSupplier) {
	        this.clientSupplier = clientSupplier;
	 }

	 public WebTarget getClient(TestUser user) throws Exception {
	        return clientSupplier.getClient(user);
	 }
	
	 public List<RfrNotesDto> getAllNotes(int actionItemId) throws Exception {

		 return Arrays.asList(getClient(RFR_ADMIN).path("/action-items/notes")
				 	.queryParam("actionItemId", actionItemId)
	                .request()
	                .get(RfrNotesDto[].class));
	    }

	 public RfrNotesDto createNote(SsoUser user,
              int actionItemId,
             RfrNotesDto rfrNotesDto) throws Exception {

		 return getClient(RFR_ADMIN).path("/action-items/notes")
				 .queryParam("actionItemId", actionItemId)
	                .request()
	                .post(Entity.json(rfrNotesDto), RfrNotesDto.class);
	 }
}
