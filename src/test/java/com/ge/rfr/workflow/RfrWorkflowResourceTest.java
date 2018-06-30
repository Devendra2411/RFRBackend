package com.ge.rfr.workflow;

import static com.ge.rfr.helper.TestUser.RFR_ADMIN;
import static com.ge.rfr.helper.TestUser.RFR_COORDINATOR_INDIA;
import static com.ge.rfr.helper.TestUser.RFR_COORDINATOR_ASIA;
import static com.ge.rfr.helper.TestUser.RFR_TEAM_MEMBER;
import static com.ge.rfr.helper.TestUser.RFR_TIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.testng.annotations.Test;

import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.auth.UserRole;
import com.ge.rfr.common.errors.ValidationErrorDetails;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;
import com.ge.rfr.fspevent.model.TrainDataDto;
import com.ge.rfr.helper.AbstractIntegrationTest;
import com.ge.rfr.helper.ModelValidator;
import com.ge.rfr.helper.TestUser;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDetailsDto;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDto;

public class RfrWorkflowResourceTest extends AbstractIntegrationTest {

	private final RfrWorkflowClient client = new RfrWorkflowClient(this::getClient);

	private static final String[] EQUIP_SERIAL_NUMBER = { "2901GL", "2901ML", "2901GL", "2901BM" };
	private static final String[] SITE_NAME = {"Test_Site","Test_Site","Test_Site","Test_Site"};
	private static final String WORKFLOW_NAME = "Test Workflow Creation";
	private static int[] OUTAGE_ID = { 499, 500, 501, 513 };
	private static String[] TRAIN_ID = { "49", "49", "49", "50" };

	private User assignedUser = new User();

	private RfrWorkflowDto workflowDto;

	/**
	 * Get all the Blocks for Specific Site
	 */
	@Test
	public void testGetBlocksForAdmin() throws Exception {
		RfrWorkflowDetailsDto rfrWorkflowDetailsDto = new RfrWorkflowDetailsDto();
		rfrWorkflowDetailsDto.setSiteName(SITE_NAME[0]);
		List<TrainDataDto> trainDataDto = client.getBlocks(RFR_ADMIN,rfrWorkflowDetailsDto);
		assertNotEquals(trainDataDto.size(), 0);
	}
	
	@Test
	public void testGetBlocksForCoordinator() throws Exception {
		RfrWorkflowDetailsDto rfrWorkflowDetailsDto = new RfrWorkflowDetailsDto();
		rfrWorkflowDetailsDto.setSiteName(SITE_NAME[0]);
		List<TrainDataDto> trainDataDto = client.getBlocks(RFR_COORDINATOR_ASIA,rfrWorkflowDetailsDto);
		assertEquals(trainDataDto.size(), 2);
	}
	
	/**
	 * Get all the Site Names Based on Role ADMIN will be getting all sites where as
	 * COORDINATOR will be getting sites of his region
	 */
	@Test
	public void testGetSitesForAdmin() throws Exception {
		List<RfrWorkflowDetailsDto> siteNamesList = client.getSites(RFR_ADMIN);
		assertNotEquals(siteNamesList.size(), 2);
	}

	@Test
	public void testGetSitesForCoordinator() throws Exception {
		List<RfrWorkflowDetailsDto> siteNamesList = client.getSites(RFR_COORDINATOR_INDIA);
		assertEquals(siteNamesList.size(), 1);
	}

	/**
	 * Creates a new workflow and validates the created workflow
	 */
	@Test(dependsOnMethods = {"testGetBlocksForAdmin","testGetBlocksForCoordinator"})
	public void testCreateRfrWorkflow() throws Exception {
		System.out.println("in testCreateRfrWorkflow");
		List<RfrWorkflowDto> rfrWorkflowDtoList = createCreateRfrWorkflowList();
		rfrWorkflowDtoList = client.createRfrWorkflow(RFR_ADMIN, rfrWorkflowDtoList);
		workflowDto = rfrWorkflowDtoList.get(0);
		for (int i = 0; i < rfrWorkflowDtoList.size(); i++) {
			validateRfrWorkflow(rfrWorkflowDtoList.get(i), i);
		}
	}
	
	@Test(expectedExceptions=BadRequestException.class)
	public void testCreateRfrWorkflowWithEmptyList() throws Exception {
		System.out.println("in testCreateRfrWorkflowWithEmptyList");
		List<RfrWorkflowDto> rfrWorkflowDtoList = new ArrayList<RfrWorkflowDto>();
		client.createRfrWorkflow(RFR_ADMIN, rfrWorkflowDtoList);
	}

	/**
	 * Tries to create a workflow with duplicate combination of ESN and Outage ID
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow")
	public void testCreateDuplicateESNAndOutage() throws Exception {

		// Change the name to lower-case and create it again to verify
		// that serial number
		List<RfrWorkflowDto> rfrWorkflowDtoList = createCreateRfrWorkflowList();
		ValidationErrorDetails errorDetails = assumeValidationError(
				() -> client.createRfrWorkflow(RFR_ADMIN, rfrWorkflowDtoList));
		assertThat(errorDetails.getFields().containsKey("outageId"));

	}

	/**
	 * checking constraint violations
	 */
	@Test
	public void testCheckConstraintViolations() throws Exception {
		RfrWorkflowDto rfrWorkflowDto = createCreateRfrWorkflowObj();
		rfrWorkflowDto.setEquipSerialNumber(null);
		rfrWorkflowDto.setSiteName(null);
		ModelValidator validator = new ModelValidator();
		validator.checkCountOfConstraintViolations(rfrWorkflowDto, 2);
	}

	/**
	 * Tries creating a workflow with a user that doesn't have the RFR_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testCreateRfrWorkflowWithoutPrivilege() throws Exception {
		client.createRfrWorkflow(RFR_TEAM_MEMBER, createCreateRfrWorkflowList());
	}

	/**
	 * Retrieve the workflow we've created earlier.
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow")
	public void testGetRfrWorkflowDetails() throws Exception {
		RfrWorkflowDto rfrWorkflowDto = client.getRfrWorkflowDetails(this.workflowDto.getWorkflowId());
		validateRfrWorkflow(rfrWorkflowDto, 0);
	}

	/**
	 * Check that trying to get a workflow with an invalid id results in a 404 with
	 * a proper message.
	 */
	@Test(expectedExceptions = NotFoundException.class)
	public void testGetUnknownRfrWorkflow() throws Exception {
		client.getRfrWorkflowDetails(99999999);
	}

	/**
	 * Tests that a workflow information can be updated Only users can be
	 * assigned\deleted while updating
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow", priority = 150)
	public void testUpdateWorkflow() throws Exception {

		// Update the project and check the immediate result
		System.out.println("in update workflow ::"+workflowDto.getWorkflowId());
		RfrWorkflowDto postUpdateDto = client.updateRfrWorkflow(TestUser.ALL_PRIVILEGES, workflowDto.getWorkflowId(),
				createUpdatedWorkflow(workflowDto));
		//validateChangedWorkflow(postUpdateDto);

		// Retrieve the updated project and check that the values match what we
		// wrote
		//postUpdateDto = client.getRfrWorkflowDetails(workflowDto.getWorkflowId());
		validateChangedWorkflow(postUpdateDto);
	}

	/**
	 * Tests that a workflow information can be updated Only users can be
	 * assigned\deleted while updating
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow", expectedExceptions = NotFoundException.class)
	public void testUpdateUnknownWorkflow() throws Exception {

		// Update the project and check the immediate result
		client.updateRfrWorkflow(TestUser.ALL_PRIVILEGES, 1234, createUpdatedWorkflow(workflowDto));

	}

	/**
	 * Get the list of assigned workflows for the user
	 * @throws Exception 
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow")
	public void testGetWorkflowList() throws Exception {
		List<RfrWorkflowDetailsDto> createdWorkflowDetails;
			createdWorkflowDetails = client.getRfrWorkflowList(RFR_TIM);
			createdWorkflowDetails.forEach(obj->assertNotEquals(obj.getWorkflowId(),null));
			assertNotEquals(createdWorkflowDetails.size(), 0);	
	}

	/**
	 * Tries to get the list of unassigned workflows for the user
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow")
	public void testGetUnAssignedWorkflowList() throws Exception {
		List<RfrWorkflowDetailsDto> createdWorkflowDetails = client.getRfrWorkflowList(TestUser.RFR_TEAM_MEMBER);
		createdWorkflowDetails.forEach(obj->assertNotEquals(obj.getWorkflowId(),null));
		assertEquals(createdWorkflowDetails.size(), 0);
	}

	/**
	 * Tries to get the list of created and assigned workflows for the admin
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow")
	public void testGetWorkflowListForAdmin() throws Exception {
		List<RfrWorkflowDetailsDto> createdWorkflowDetails = client.getRfrWorkflowList(TestUser.RFR_ADMIN);
		createdWorkflowDetails.forEach(obj->assertNotEquals(obj.getWorkflowId(),null));
		assertNotEquals(createdWorkflowDetails.size(), 0);
	}

	/**
	 * Tries to get the list of created and assigned workflows for the RFR
	 * Coordinator
	 */
	@Test(dependsOnMethods = "testCreateRfrWorkflow")
	public void testGetWorkflowListForCoordinator() throws Exception {
		List<RfrWorkflowDetailsDto> createdWorkflowDetails = client.getRfrWorkflowList(TestUser.RFR_COORDINATOR_INDIA);
		createdWorkflowDetails.forEach(obj->assertNotEquals(obj.getWorkflowId(),null));
		assertNotEquals(createdWorkflowDetails.size(), 0);
	}

	private List<RfrWorkflowDto> createCreateRfrWorkflowList() {
		List<RfrWorkflowDto> rfrWorkflowDtoList = new ArrayList<RfrWorkflowDto>();
		for (int i = 0; i < OUTAGE_ID.length; i++) {
			RfrWorkflowDto rfrWorkflowDto = new RfrWorkflowDto();
			rfrWorkflowDto.setEquipSerialNumber(EQUIP_SERIAL_NUMBER[i]);
			rfrWorkflowDto.setOutageId(OUTAGE_ID[i]);
			rfrWorkflowDto.setSiteName(SITE_NAME[i]);
			rfrWorkflowDto.setTrainId(TRAIN_ID[i]);
			rfrWorkflowDto.setWorkflowName(WORKFLOW_NAME);
			assignedUser.setSso("10000001");
			assignedUser.setFirstName("tim");
			assignedUser.setLastName("Engineer");
			List<User> engineerList = new ArrayList<User>();
			engineerList.add(assignedUser);
			rfrWorkflowDto.setAssignedEngineers(engineerList);
			rfrWorkflowDto.setChangeTracking(getChangeTrackingObj());
			rfrWorkflowDtoList.add(rfrWorkflowDto);
		}

		return rfrWorkflowDtoList;

	}

	private RfrWorkflowDto createCreateRfrWorkflowObj() {
		RfrWorkflowDto rfrWorkflowDto = new RfrWorkflowDto();
		rfrWorkflowDto.setEquipSerialNumber(EQUIP_SERIAL_NUMBER[0]);
		rfrWorkflowDto.setOutageId(OUTAGE_ID[0]);
		rfrWorkflowDto.setSiteName(SITE_NAME[0]);
		rfrWorkflowDto.setTrainId(TRAIN_ID[0]);
		rfrWorkflowDto.setWorkflowName(WORKFLOW_NAME);
		assignedUser.setSso("10000001");
		assignedUser.setFirstName("tim");
		assignedUser.setLastName("Engineer");
		List<User> engineerList = new ArrayList<User>();
		engineerList.add(assignedUser);
		rfrWorkflowDto.setAssignedEngineers(engineerList);
		return rfrWorkflowDto;
	}

	private void validateRfrWorkflow(RfrWorkflowDto rfrWorkflowDto, int index) {
		assertNotEquals(0, rfrWorkflowDto.getWorkflowId());
		assertEquals(EQUIP_SERIAL_NUMBER[index], rfrWorkflowDto.getEquipSerialNumber());
		assertEquals(OUTAGE_ID[index], rfrWorkflowDto.getOutageId());
		assertEquals(TRAIN_ID[index], rfrWorkflowDto.getTrainId());
		assertEquals(SITE_NAME[index], rfrWorkflowDto.getSiteName());
		assertEquals(1, rfrWorkflowDto.getAssignedEngineers().size());
		assertTrue(rfrWorkflowDto.getAssignedEngineers().contains(assignedUser));
		validatePristineChangeTracking(rfrWorkflowDto.getChangeTracking(), TestUser.RFR_ADMIN);
	}

	// Validate the properties of the changed workflow after being updated
	private void validateChangedWorkflow(RfrWorkflowDto postUpdateDto) {
		// Validate the updated project
		assertEquals(workflowDto.getWorkflowId(), postUpdateDto.getWorkflowId());
		assertEquals(workflowDto.getEquipSerialNumber(), postUpdateDto.getEquipSerialNumber());
		assertEquals(workflowDto.getSiteName(), postUpdateDto.getSiteName());
		assertEquals(workflowDto.getOutageId(), postUpdateDto.getOutageId());
		assertEquals(1, postUpdateDto.getAssignedEngineers().size());
		assertEquals("503055666",postUpdateDto.getAssignedEngineers().get(0).getSso());
		validateModifiedChangeTracking(postUpdateDto.getChangeTracking(), workflowDto.getChangeTracking(),
				TestUser.ALL_PRIVILEGES);
	}

	private RfrWorkflowDto createUpdatedWorkflow(RfrWorkflowDto postUpdatedDto) {
		User newUser = new User();
		newUser.setSso("503055666");
		newUser.setFirstName("TestFirst");
		newUser.setLastName("TestLast");
		List<User> engineerList = new ArrayList<User>();
		engineerList.add(newUser);
		postUpdatedDto.setAssignedEngineers(engineerList);
		return postUpdatedDto;
	}

	private ChangeTracking getChangeTrackingObj() {
		Collection<UserRole> roles = new ArrayList<UserRole>();
		roles.add(UserRole.RFR_ADMIN);
		SsoUser newUser = new SsoUser("503055897", "Devendra", "Tummala", "503055897@ge.com", roles);
		return new ChangeTracking(User.fromSsoUser(newUser));
	}
}
