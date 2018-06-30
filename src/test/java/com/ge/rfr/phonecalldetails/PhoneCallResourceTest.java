package com.ge.rfr.phonecalldetails;

import static com.ge.rfr.helper.TestUser.RFR_ADMIN;
import static com.ge.rfr.helper.TestUser.RFR_COORDINATOR_EUROPE;
import static com.ge.rfr.helper.TestUser.RFR_COORDINATOR_ASIA;
import static com.ge.rfr.helper.TestUser.RFR_TEAM_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.assertj.core.data.MapEntry;
import org.assertj.guava.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ge.rfr.common.errors.ValidationErrorDetails;
import com.ge.rfr.common.errors.field.FieldErrors;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;
import com.ge.rfr.helper.AbstractIntegrationTest;
import com.ge.rfr.helper.ModelValidator;
import com.ge.rfr.helper.TestUser;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDetailsDto;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDto;
import com.ge.rfr.workflow.RfrWorkflowClient;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDto;

public class PhoneCallResourceTest extends AbstractIntegrationTest{
	
	private final PhoneCallDetailsClient client = new PhoneCallDetailsClient(this::getClient);
	private final RfrWorkflowClient workflowClient = new RfrWorkflowClient(this::getClient);
	
	private static final String MEETING_LINE = "Meeting_Subject";
	
	private static final String EQUIP_SERIAL_NUMBER = "2901GM" ;
	private static final String SITE_NAME = "Test_Site";
	private static final String WORKFLOW_NAME = "Test Workflow Creation";
	private static int OUTAGE_ID = 599;
	private static String TRAIN_ID = "51";
	
	private PhoneCallDetailsDto callDetailsDto , regionCallDetailsDto;
	
	private RfrWorkflowDto workflowDto;
	
	Instant now = Instant.now();
	
	Random random = new Random();
	String randomString = "xyz";
	
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
	 * Creates a new Phone Call Line Item and validates the created  line item
	 */
	@Test
	public void testCreatePhoneCall() throws Exception
	{
		PhoneCallDetailsDto phoneCallDetailsDto = createCreatePhoneCall(MEETING_LINE);
		callDetailsDto = client.createPhoneCall(RFR_ADMIN,workflowDto.getWorkflowId(),phoneCallDetailsDto);
		validatePhoneCall(callDetailsDto,MEETING_LINE,TestUser.RFR_ADMIN);
	}
	
	/**
	 * Creates a new Phone Call Line Item with CoOrdinater Role and validates the created  line item
	 */
	@Test
	public void testCreatePhoneCallWithCoOrdinatorRole() throws Exception
	{
		PhoneCallDetailsDto phoneCallDetailsDto = createCreatePhoneCall("Meeting With CoOrdinator");
	    regionCallDetailsDto = client.createPhoneCall(RFR_COORDINATOR_ASIA,
																		  workflowDto.getWorkflowId(),
																		  phoneCallDetailsDto);
		validatePhoneCall(regionCallDetailsDto,
						  "Meeting With CoOrdinator",
						  RFR_COORDINATOR_ASIA);
	}
	
	/**
	 * Creates a new Phone Call Line Item with Coordinator
	 * role who does not have access to that region
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testCreatePhoneCallWithUnknownCoOrdinator() throws Exception
	{
		PhoneCallDetailsDto phoneCallDetailsDto = createCreatePhoneCall(MEETING_LINE);
		callDetailsDto = client.createPhoneCall(RFR_COORDINATOR_EUROPE,workflowDto.getWorkflowId(),phoneCallDetailsDto);
		
	}
	
	/**
	 * Tries to create a meeting with a name that already exists and checks that
	 * the meeting creation fails with the correct error code.
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall")
	public void testCreatePhoneCallWithDuplicateName() throws Exception {

		// Change the name to lower-case and create it again to verify
		// that meeting names are validated
		PhoneCallDetailsDto createDto = createCreatePhoneCall(MEETING_LINE);
		createDto.setMeetingLine(createDto.getMeetingLine().toLowerCase());
		ValidationErrorDetails errorDetails = assumeValidationError(
				() -> client.createPhoneCall(RFR_ADMIN,workflowDto.getWorkflowId(),createDto));
		assertThat(errorDetails.getGlobal()).isEmpty();
		Assertions.assertThat(errorDetails.getFields()).contains(MapEntry.entry("meetingLine", FieldErrors.duplicate()));

	}
	
	/**
	 * Tries to create a meeting with exceeding meeting line content and checks that
	 * the meeting creation fails with the correct error code.
	 */
	@Test
	public void testCreatePhoneCallWithExceedingMeetingLine() throws Exception
	{
		
		for (int i = 0; i < 257; i++) {
			randomString = randomString+randomString.charAt(random.nextInt(randomString.length()));
		} 
		
		PhoneCallDetailsDto createDto = createCreatePhoneCall(randomString);
		ValidationErrorDetails errorDetails = assumeValidationError(
				() -> client.createPhoneCall(RFR_ADMIN,workflowDto.getWorkflowId(),createDto));
		assertThat(errorDetails.getGlobal()).isEmpty();
		Assertions.assertThat(errorDetails.getFields()).contains(MapEntry.entry("meetingLine", FieldErrors.size(1, 255)));
		
	}
	
	/**
	 * Tries creating a phone call meeting with a user that doesn't have the PHONE_CALL_CREATE
	 * privilege
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testCreatePhoneCallWithoutPrivilege() throws Exception {
		client.createPhoneCall(RFR_TEAM_MEMBER,
							   workflowDto.getWorkflowId(),
							   createCreatePhoneCall(MEETING_LINE));
	}
	
	/**
	 * Tries creating a phone call meeting with 
	 * invalid workflowId
	 */
	@Test(expectedExceptions = NotFoundException.class)
	public void testCreateUnknownPhoneCall() throws Exception {
		client.createPhoneCall(RFR_ADMIN,
							   88888,
							   createCreatePhoneCall(MEETING_LINE));
	}
	
	/**
	 * checking constraint violations
	 */
	@Test
	public void testCheckConstraintViolations() throws Exception {
		PhoneCallDetailsDto phoneCallDetailsDto = createCreatePhoneCall(MEETING_LINE);
		phoneCallDetailsDto.setMeetingLine(" ");
		phoneCallDetailsDto.setMeetingDate(null);
		ModelValidator validator = new ModelValidator();
		validator.checkCountOfConstraintViolations(phoneCallDetailsDto, 2);
	}

	@DataProvider(name = "providePhoneCallMinutesContent")
	public Object[][] providePhoneCallMinutesContent() {
		return new Object[][]{{"Meeting Content"}};
	}
	
	/**
	 * Tests that a phone call meeting information can be updated
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall", priority = 200, dataProvider = "providePhoneCallMinutesContent")
	public void testUpdatePhoneCall(String meetingContent) throws Exception
	{
		PhoneCallDetailsDto postUpdateDto = client.updatePhoneCall(TestUser.RFR_ADMIN,
																   workflowDto.getWorkflowId(),
																   callDetailsDto.getMeetingId(),
																   createUpdatedPhoneCall(meetingContent));
		validateUpdatedPhoneCall(postUpdateDto);
		
	}
	
	@DataProvider(name = "providePhoneCallMinutesEmpty")
	public Object[][] providePhoneCallMinutesEmpty() {
	return new Object[][]{{""}};
	}
	
	/**
	 * Tries to update Phone call minutes with empty minutes content and
	 * checks that the appropriate exception is thrown
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall", 
		  dataProvider = "providePhoneCallMinutesEmpty")
	public void testUpdatePhoneCallWithEmptyMinutes(String meetingContent) throws Exception
	{
		ValidationErrorDetails errorDetails = assumeValidationError(
				() ->client.updatePhoneCall(TestUser.RFR_ADMIN,
								workflowDto.getWorkflowId(),
								callDetailsDto.getMeetingId(),
								createUpdatedPhoneCall(meetingContent)));
		assertThat(errorDetails.getFields().containsKey("phoneCallMinutes"));	
	}
	
	/**
	 * Tries to update Phone call details by a CoOrdinator who
	 * doesn't have access to the asia region
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall", expectedExceptions = ForbiddenException.class,
		  dataProvider = "providePhoneCallMinutesEmpty")
	public void testUpdatePhoneCallWithUnkownCoOrdinatorRole(String meetingContent) throws Exception
	{
		 meetingContent="Testing testUpdatePhoneCallWithUnkownCoOrdinatorRole";
		 client.updatePhoneCall(TestUser.RFR_COORDINATOR_EUROPE,
								workflowDto.getWorkflowId(),
								callDetailsDto.getMeetingId(),
								createUpdatedPhoneCall(meetingContent));
		
	}
	
	/**
	 * Tests that a phone call meeting information can be updated
	 * using invalid workflowId
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall", expectedExceptions = NotFoundException.class ,dataProvider = "providePhoneCallMinutesContent")
	public void testUpdateCallWithInvalidWorkflow(String meetingContent) throws Exception
	{
			client.updatePhoneCall(TestUser.RFR_ADMIN,
								   9999,
								   callDetailsDto.getMeetingId(),
								   createUpdatedPhoneCall(meetingContent));
	}
	
	/**
	 * Tests that a phone call meeting information can be updated
	 * using valid workflowId and invalid meetingId
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall", expectedExceptions = NotFoundException.class,dataProvider = "providePhoneCallMinutesContent")
	public void testUpdateCallWithInvalidMeeting(String meetingContent) throws Exception
	{
			client.updatePhoneCall(TestUser.RFR_ADMIN,
								   workflowDto.getWorkflowId(),
								   99999,
								   createUpdatedPhoneCall(meetingContent));
	}
	
	/**
	 * Tries to update a meeting with exceeding meeting line content and checks that
	 * the meeting update fails with the correct error code.
	 */
	
	@Test(dependsOnMethods = "testCreatePhoneCall",dataProvider = "providePhoneCallMinutesContent")
	public void testUpdatePhoneCallWithExceedingMeetingLine(String meetingContent) throws Exception
	{
		PhoneCallDetailsDto postUpdateDto = createUpdatedPhoneCall(meetingContent);
				   													
		for (int i = 0; i < 257; i++) {
			randomString = randomString+randomString.charAt(random.nextInt(randomString.length()));
		} 
		
		postUpdateDto.setMeetingLine(randomString);
														
		ValidationErrorDetails errorDetails = assumeValidationError(
					() -> client.updatePhoneCall(TestUser.RFR_ADMIN,
												 workflowDto.getWorkflowId(),
												 callDetailsDto.getMeetingId(),
												 postUpdateDto));
		assertThat(errorDetails.getFields().containsKey("meetingLine"));
		
	}
	
	/**
	 * Gets all the Phone Call Meetings with the given Workflow Id
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall")
	public void testGetPhoneCallItems() throws Exception
	{
		PhoneCallDto phoneCallDto = client.getPhoneCallItems(TestUser.RFR_ADMIN,
															 workflowDto.getWorkflowId());
		assertEquals(phoneCallDto.getPhoneCallDetailsDtoList().size(),2);
	}
	
	/**
	 * Tries to retrieve the phone call line items with
	 * the coordinator who has access to the Asia region
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall")
	public void testGetPhoneCallItemsForCoOrdinator() throws Exception
	{
		PhoneCallDto phoneCallDto = client.getPhoneCallItems(RFR_COORDINATOR_ASIA,
															 workflowDto.getWorkflowId());
		assertEquals(phoneCallDto.getPhoneCallDetailsDtoList().size(),2);
	}
	
	/**
	 * Tries to retrieve the phone call line items with
	 * the europe coordinator who doesn't have access to the asia region
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall",expectedExceptions = ForbiddenException.class)
	public void testGetCallItemsForUnknownCoOrdinator() throws Exception
	{
		 client.getPhoneCallItems(TestUser.RFR_COORDINATOR_EUROPE,
								  workflowDto.getWorkflowId());
		
	}
	
	/**
	 * Tries to retrieve the phone call line items with
	 * invalid workflowId
	 */
	@Test(expectedExceptions = NotFoundException.class)
	public void testGetUnknownPhoneCallItems() throws Exception
	{
		client.getPhoneCallItems(TestUser.RFR_TIM,99999);
	}
	
	/**
	 * Retrieve the phone call meeting we've created earlier with Admin Role.
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall")
	public void testGetPhoneCallDetails() throws Exception
	{
		PhoneCallDto phoneCallDto = client.getPhoneCallDetails(TestUser.RFR_ADMIN,workflowDto.getWorkflowId(),
															   callDetailsDto.getMeetingId());
		validatePhoneCall(phoneCallDto.getPhoneCallDetailsDtoList().get(0), 
						  MEETING_LINE,TestUser.RFR_ADMIN);
	}
	
	/**
	 * Retrieve the phone call meeting we've created earlier with CoOrdinator Role.
	 */
	@Test(dependsOnMethods = "testCreatePhoneCall")
	public void testGetPhoneCallDetailsForCoOrdinator() throws Exception
	{
		System.out.println("Role"+regionCallDetailsDto.getMeetingId());
		PhoneCallDto phoneCallDto = client.getPhoneCallDetails(RFR_COORDINATOR_ASIA ,workflowDto.getWorkflowId(),
															   regionCallDetailsDto.getMeetingId());
		validatePhoneCall(phoneCallDto.getPhoneCallDetailsDtoList().get(0), 
						  "Meeting With CoOrdinator", RFR_COORDINATOR_ASIA);
	}
	
	/**
	 * Tries to retrieve the phone call meeting with
	 * invalid workflowId and invalid meetingId
	 */
	@Test(expectedExceptions = NotFoundException.class)
	public void testGetUnknownPhoneCallDetiails() throws Exception
	{
		client.getPhoneCallDetails(TestUser.RFR_TIM,99999,99999);
	}
	
	/**
	 * Tries to retrieve the phone call meeting with
	 * the coordinator who doesn't have access to the region
	 */
	@Test(expectedExceptions = ForbiddenException.class)
	public void testGetCallDetiailsForUnknownCoOrdinator() throws Exception
	{
		client.getPhoneCallDetails(TestUser.RFR_COORDINATOR_EUROPE,workflowDto.getWorkflowId(),
								  callDetailsDto.getMeetingId());
	}
	
	/**
	 * Tries to retrieve the phone call meeting with
	 * valid workflowId and invalid meetingId
	 */
	@Test(expectedExceptions = NotFoundException.class)
	public void testGetPhoneCallDetiailsWithInvalidMeeting() throws Exception
	{
		client.getPhoneCallDetails(TestUser.RFR_ADMIN,workflowDto.getWorkflowId(),99999);
	}
	
	
	private PhoneCallDetailsDto createCreatePhoneCall(String meeting) {
		
		PhoneCallDetailsDto createDto = new PhoneCallDetailsDto();
		createDto.setMeetingLine(meeting);
		createDto.setMeetingDate(now);
		
		return createDto;
	}
	
	private void validatePhoneCall(PhoneCallDetailsDto callDetailsDto,
								   String meeting,
								   TestUser role) {
		
		assertNotEquals(0, callDetailsDto.getMeetingId());
		assertEquals(meeting,callDetailsDto.getMeetingLine());
		assertEquals(now,callDetailsDto.getMeetingDate());
		validatePristineChangeTracking(callDetailsDto.getChangeTracking(), role);
	}
	
	private void validateUpdatedPhoneCall(PhoneCallDetailsDto postUpdateDto) {
		
		// Validate the updated project
		assertEquals(callDetailsDto.getMeetingId(), postUpdateDto.getMeetingId());
		assertEquals(callDetailsDto.getMeetingLine() + "_Changed", postUpdateDto.getMeetingLine());
		assertThat(postUpdateDto.getMeetingDate()).isEqualTo(Instant.parse("2018-10-23T10:12:12Z"));
		assertEquals("Meeting Content",postUpdateDto.getPhoneCallMinutes());
		validateModifiedChangeTracking(postUpdateDto.getChangeTracking(), callDetailsDto.getChangeTracking(),
									   TestUser.RFR_ADMIN);
		
	}

	private PhoneCallDetailsDto createUpdatedPhoneCall(String meetingContent) {
		
		PhoneCallDetailsDto phoneCallDetailsDto = new PhoneCallDetailsDto();
		phoneCallDetailsDto.setMeetingId(callDetailsDto.getMeetingId());
		phoneCallDetailsDto.setMeetingLine("Meeting_Subject_Changed");
		phoneCallDetailsDto.setMeetingDate(Instant.parse("2018-10-23T10:12:12Z"));
		phoneCallDetailsDto.setPhoneCallMinutes(meetingContent);
		phoneCallDetailsDto.setChangeTracking(new ChangeTracking(User.fromSsoUser(TestUser.RFR_ADMIN.getSsoUser())));
		
		return phoneCallDetailsDto;
		
	}
	
}
