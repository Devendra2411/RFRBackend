package com.ge.rfr.actionitem.notes;

import static com.ge.rfr.helper.TestUser.RFR_ADMIN;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.testng.annotations.Test;

import com.ge.rfr.actionitem.RfrActionItemClient;
import com.ge.rfr.actionitem.model.dto.RfrActionItemDto;
import com.ge.rfr.actionitem.notes.model.dto.RfrNotesDto;
import com.ge.rfr.common.model.User;
import com.ge.rfr.helper.AbstractIntegrationTest;
import com.ge.rfr.helper.TestUser;
import com.ge.rfr.workflow.RfrWorkflowClient;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDto;

public class RfrNotesResourceTest extends AbstractIntegrationTest{

private final RfrNotesClient client = new RfrNotesClient(this::getClient);
private final RfrActionItemClient actionItemClient = new RfrActionItemClient(this::getClient);
private final RfrWorkflowClient workflowClient = new RfrWorkflowClient(this::getClient);
	
	private static String NOTES = "Testing";
	private static final String EQUIP_SERIAL_NUMBER = "2901GM" ;
	private static final String SITE_NAME = "Test_Site";
	private static final String WORKFLOW_NAME = "Test Workflow Creation";
	private static int OUTAGE_ID = 601;
	private static String TRAIN_ID = "51";
	
	private static String ACTION_ITEM_TITLE = "Task Created for Workflow";
	
	private RfrWorkflowDto workflowDto;
	private RfrActionItemDto actionItemDto;

	
	private RfrNotesDto rfrNotesDto;
	
	@Test
	public void testCreateRfrWorkflow() throws Exception {
		RfrWorkflowDto rfrWorkflowDto = createCreateRfrWorkflow();
		List<RfrWorkflowDto> rfrWorkflowDtoList=new ArrayList<RfrWorkflowDto>();
		rfrWorkflowDtoList.add(rfrWorkflowDto);
		rfrWorkflowDtoList = workflowClient.createRfrWorkflow(RFR_ADMIN, rfrWorkflowDtoList);
		workflowDto=rfrWorkflowDtoList.get(0);
		
		actionItemDto=actionItemClient.getRfrActionItemsList(RFR_ADMIN, workflowDto.getWorkflowId()).getActionItemsList().get(0);
	}
	
	private RfrWorkflowDto createCreateRfrWorkflow() {
		RfrWorkflowDto rfrWorkflowDto = new RfrWorkflowDto();
		rfrWorkflowDto.setEquipSerialNumber(EQUIP_SERIAL_NUMBER);
		rfrWorkflowDto.setOutageId(OUTAGE_ID);
		rfrWorkflowDto.setSiteName(SITE_NAME);
		rfrWorkflowDto.setTrainId(TRAIN_ID);
		rfrWorkflowDto.setWorkflowName(WORKFLOW_NAME);
		User assignedUser = new User();
		assignedUser.setSso("10000001");
		assignedUser.setFirstName("tim");
		assignedUser.setLastName("Engineer");
		List<User> engineerList = new ArrayList<User>();
		engineerList.add(assignedUser);	
		rfrWorkflowDto.setAssignedEngineers(engineerList);
		return rfrWorkflowDto;
		
	}
	
	
	/**
	 * Get all Notes of Specific Action Item
	 */
	@Test(dependsOnMethods="testCreateNotes")
	public void testGetAllNotes() throws Exception
	{
		List<RfrNotesDto> actualCountOfNotes = client
				.getAllNotes(actionItemDto.getActionItemId());
		assertEquals(actualCountOfNotes.size(), 1);
	}
	
	/**
	 * Get all Notes of Specific Action Item
	 */
	@Test(dependsOnMethods="testCreateNotes", expectedExceptions = NotFoundException.class)
	public void testGetAllNotesWithUnknownActionItem() throws Exception
	{
		List<RfrNotesDto> actualCountOfNotes = client
				.getAllNotes(432321);
		assertEquals(actualCountOfNotes.size(), 1);
	}
	
	/**
	 * Create Notes for specific Action Item 
	 */
	@Test(dependsOnMethods="testCreateRfrWorkflow")
	public void  testCreateNotes() throws Exception
	{
		rfrNotesDto = client.createNote(TestUser.RFR_TIM.getSsoUser(), 
				actionItemDto.getActionItemId(), createCreateNotes());
		validateCreateNotes(rfrNotesDto);
	}
	
	@Test(expectedExceptions = NotFoundException.class)
	public void  testCreateNotesWithUnknownActionItem() throws Exception
	{
		rfrNotesDto = client.createNote(TestUser.RFR_TIM.getSsoUser(), 
				23212, createCreateNotes());
		validateCreateNotes(rfrNotesDto);
	}
	
	
	private void validateCreateNotes(RfrNotesDto rfrNotesDto)
	{
		assertNotEquals(rfrNotesDto.getNotesId(), 0);
		assertEquals(rfrNotesDto.getNotes(), NOTES);
		assertEquals(rfrNotesDto.getActionItemId(), 
				actionItemDto.getActionItemId());
	}
	
	private RfrNotesDto createCreateNotes()
	{
		RfrNotesDto notesDto=new RfrNotesDto();
		notesDto.setNotes(NOTES);
		return notesDto;
	}
}
