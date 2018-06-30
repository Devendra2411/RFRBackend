package com.ge.rfr.actionitem;

import static com.ge.rfr.helper.TestUser.RFR_ADMIN;
import static com.ge.rfr.helper.TestUser.RFR_TEAM_MEMBER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ge.rfr.actionitem.model.Category;
import com.ge.rfr.actionitem.model.Owner;
import com.ge.rfr.actionitem.model.Status;
import com.ge.rfr.actionitem.model.TaskType;
import com.ge.rfr.actionitem.model.dto.RfrActionItemDetailsDto;
import com.ge.rfr.actionitem.model.dto.RfrActionItemDto;
import com.ge.rfr.common.model.User;
import com.ge.rfr.helper.AbstractIntegrationTest;
import com.ge.rfr.helper.TestUser;
import com.ge.rfr.workflow.RfrWorkflowClient;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDto;

public class RfrActionItemResourceTest extends AbstractIntegrationTest{
	
	private final RfrActionItemClient client = new RfrActionItemClient(this::getClient);
	private final RfrWorkflowClient workflowClient = new RfrWorkflowClient(this::getClient);
	
	private RfrWorkflowDto workflowDto;
	
	private RfrActionItemDto rfrActionItem;
	
	private static String ACTION_ITEM_TITLE = "Task Created for Workflow";
	private static final String EQUIP_SERIAL_NUMBER = "2901GM" ;
	private static final String SITE_NAME = "Test_Site";
	private static final String WORKFLOW_NAME = "Test Workflow Creation";
	private static int OUTAGE_ID = 600;
	private static String TRAIN_ID = "51";
	
	@BeforeClass
	public void testCreateRfrWorkflow() throws Exception {
		RfrWorkflowDto rfrWorkflowDto = createCreateRfrWorkflow();
		List<RfrWorkflowDto> rfrWorkflowDtoList=new ArrayList<RfrWorkflowDto>();
		rfrWorkflowDtoList.add(rfrWorkflowDto);
		rfrWorkflowDtoList = workflowClient.createRfrWorkflow(RFR_ADMIN, rfrWorkflowDtoList);
		workflowDto=rfrWorkflowDtoList.get(0);
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
	 * Creates a new workflow and validates the created workflow
	 */
	@Test
	public void testCreateActionItem() throws Exception {
		RfrActionItemDto actionItemDto = createCreateActionItem();
		rfrActionItem = client.createRfrActionItem(RFR_ADMIN, workflowDto.getWorkflowId(), actionItemDto);
		validateRfrActionItem(rfrActionItem);
	}
	
	/**
	 * Tries creating a action item with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testCreateActionItemWithoutPrivilege() throws Exception {
		client.createRfrActionItem(RFR_TEAM_MEMBER, workflowDto.getWorkflowId(), createCreateActionItem());
	}
	
	/**
	 * Tries creating a action item with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testCreateActionItemWithoutRegionPrivilege() throws Exception {
		client.createRfrActionItem(TestUser.RFR_COORDINATOR_EUROPE, workflowDto.getWorkflowId(), createCreateActionItem());
	}
	
	/**
	 * Tries creating a action item with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = NotFoundException.class)
	public void testCreateActionItemWithUnknownWorkflow() throws Exception {
		client.createRfrActionItem(TestUser.RFR_ADMIN, 645454, createCreateActionItem());
	}
	
	/**
	 * Tests that a action item information can be updated 
	 */
	@Test(dependsOnMethods = "testCreateActionItem", priority = 150)
	public void testUpdateActionItem() throws Exception 
	{

		// Update the project and check the immediate result
		RfrActionItemDto updatedActionItemDto = client.updateRfrActionItem(TestUser.ALL_PRIVILEGES,
																		   workflowDto.getWorkflowId(),
																		   rfrActionItem.getActionItemId(), 
																		   createUpdatedActionItem());
		validateChangedRfrActionItem(updatedActionItemDto);
	}
	
	/**
	 * Tries creating a workflow with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testUpdateActionItemWithoutPrivilege() throws Exception {
		client.updateRfrActionItem(RFR_TEAM_MEMBER,workflowDto.getWorkflowId(), rfrActionItem.getActionItemId(), createUpdatedActionItem());
	}
	
	/**
	 * Tries creating a action item with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testUpdateActionItemWithoutRegionPrivilege() throws Exception {
		client.updateRfrActionItem(TestUser.RFR_COORDINATOR_EUROPE,
								   workflowDto.getWorkflowId(),
								   rfrActionItem.getActionItemId(),
								   createCreateActionItem());
	}
	
	/**
	 * Tries creating a workflow with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = NotFoundException.class)
	public void testUpdateActionItemWithUnknownActionItem() throws Exception {
		client.updateRfrActionItem(TestUser.RFR_ADMIN,workflowDto.getWorkflowId(), 1234, createUpdatedActionItem());
	}
	
	@Test
	public void testGetActionItemsList() throws Exception
	{
		RfrActionItemDetailsDto actionItems = client.getRfrActionItemsList(TestUser.RFR_ADMIN, workflowDto.getWorkflowId());
		assertEquals(actionItems.getEquipSerialNumber(), workflowDto.getEquipSerialNumber());
		assertEquals(actionItems.getActionItemsList().size(), 3);
	}
	
	/**
	 * Tries creating a workflow with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testGetActionItemsListWithoutPrivilege() throws Exception {
		client.getRfrActionItemsList(TestUser.RFR_COORDINATOR_EUROPE, workflowDto.getWorkflowId());
	}
	
	private RfrActionItemDto createCreateActionItem()
	{
		RfrActionItemDto actionItem = new RfrActionItemDto();
		actionItem.setCategory(Category.CLEANLINESS);
		actionItem.setDueDate(Instant.parse("2018-12-03T10:15:30.00Z"));
		actionItem.setItemTitle(ACTION_ITEM_TITLE);
		actionItem.setLevelValue(2);
		actionItem.setOwner(Owner.OPS_CENTER);
		actionItem.setStatus(Status.COMPLETE);
		actionItem.setTaskType(TaskType.DEMOBILIZATION);
		return actionItem;
	}
	
	private RfrActionItemDto createUpdatedActionItem()
	{
		RfrActionItemDto updatedActionItem = new RfrActionItemDto();
		updatedActionItem.setItemTitle(ACTION_ITEM_TITLE + "_CHANGED");
		updatedActionItem.setDueDate(Instant.parse("2018-12-03T10:15:30.00Z"));
		updatedActionItem.setCategory(Category.GENERATOR);
		updatedActionItem.setTaskType(TaskType.FIRST_FIRE);
		updatedActionItem.setLevelValue(1);
		updatedActionItem.setOwner(Owner.OPS_CENTER);
		updatedActionItem.setStatus(Status.COMPLETE);
		return updatedActionItem;
	}
	
	private void validateRfrActionItem(RfrActionItemDto actionItemDto)
	{
		assertNotEquals(0, actionItemDto.getActionItemId());
		assertEquals(Category.CLEANLINESS, actionItemDto.getCategory());
		assertEquals(ACTION_ITEM_TITLE, actionItemDto.getItemTitle());
		assertEquals(2, actionItemDto.getLevelValue());
		assertEquals(Owner.OPS_CENTER, actionItemDto.getOwner());
		assertEquals(Status.COMPLETE, actionItemDto.getStatus());
		assertEquals(TaskType.DEMOBILIZATION, actionItemDto.getTaskType());
		assertEquals(workflowDto.getWorkflowId(), actionItemDto.getWorkflowId());
		validatePristineChangeTracking(actionItemDto.getChangeTracking(), TestUser.RFR_ADMIN);
	}
	
	private void validateChangedRfrActionItem(RfrActionItemDto updatedActionItemDto)
	{
		assertEquals(updatedActionItemDto.getActionItemId(), rfrActionItem.getActionItemId());
		assertEquals(updatedActionItemDto.getItemTitle(), ACTION_ITEM_TITLE + "_CHANGED");
		assertEquals(updatedActionItemDto.getCategory(), Category.GENERATOR);
		assertEquals(updatedActionItemDto.getTaskType(), TaskType.FIRST_FIRE);
		validateModifiedChangeTracking(updatedActionItemDto.getChangeTracking(), rfrActionItem.getChangeTracking(),
				TestUser.ALL_PRIVILEGES);
	}
	
	

}
