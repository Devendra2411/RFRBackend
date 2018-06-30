package com.ge.rfr.phonecalldetails;

import static com.ge.rfr.auth.Privileges.PHONE_CALL_CREATE;
import static com.ge.rfr.auth.Privileges.PHONE_CALL_VIEW;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDetailsDto;
import com.ge.rfr.phonecalldetails.model.dto.PhoneCallDto;
import com.ge.rfr.phonecalldetails.model.dto.ValidatorGroups.PhoneCallMinutesValidator;
import com.ge.rfr.phonecalldetails.model.dto.Views;
import com.ge.rfr.workflow.WorkflowDao;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("{workflowId}/phone-call")
@Api(tags = {"RFR-Phone Call"})
@SwaggerDefinition(tags = {@Tag(name = "RFR-Phone Call", description = "Operations pertaining to Rfr Phone Calls")})
public class PhoneCallResource {

    private final PhoneCallService phoneCallService;

    @Autowired
    public PhoneCallResource(PhoneCallService phoneCallService, WorkflowDao workflowDao) {
        this.phoneCallService = phoneCallService;
    }

    @GetMapping
    @Secured(PHONE_CALL_VIEW)
    @JsonView(Views.PhoneCallListView.class)
    @ApiOperation(value = "View a list of Phone Call Items", response = PhoneCallDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public PhoneCallDto getPhoneCallItems(@ApiIgnore SsoUser user,
                                          @PathVariable("workflowId") int workflowId) {
        return phoneCallService.getPhoneCallItems(user, workflowId);

    }

    @GetMapping("/minutes")
    @Secured(PHONE_CALL_VIEW)
    @JsonView(Views.PhoneCallDetailsView.class)
    @ApiOperation(value = "View  Phone Call Details", response = PhoneCallDetailsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public PhoneCallDto getPhoneCallDetails(@ApiIgnore SsoUser user,
                                            @PathVariable("workflowId") int workflowId,
                                            @ApiParam(value = "Meeting Id", required = true)
                                            @QueryParam("meetingId") int meetingId) {
        return phoneCallService.getPhoneCallDetails(user, workflowId, meetingId);

    }

    @PostMapping
    @Secured(PHONE_CALL_CREATE)
    @JsonView(Views.PhoneCallDetailsView.class)
    @ApiOperation(value = "Create a new Phone Call")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You are not authorized to create a Phone Call"),
    })
    public PhoneCallDetailsDto createPhoneCall(@ApiIgnore SsoUser user,
                                               @PathVariable("workflowId") int workflowId,
                                               @Valid @RequestBody PhoneCallDetailsDto createDto) {
        return PhoneCallDetailsDto.valueOf(phoneCallService.createPhoneCall(user,
                workflowId,
                createDto));
    }

    @PutMapping
    @Secured(PHONE_CALL_CREATE)
    @JsonView(Views.PhoneCallDetailsView.class)
    @ApiOperation(value = "Update a Phone Call ")
    public PhoneCallDetailsDto updatePhoneCall(@ApiIgnore SsoUser user,
                                               @PathVariable("workflowId") int workflowId,
                                               @ApiParam(value = "Meeting Id", required = true)
                                               @QueryParam("meetingId") int meetingId,
                                               @Validated(PhoneCallMinutesValidator.class)
                                               @Valid @RequestBody PhoneCallDetailsDto updatePhoneCallDetails) {

        return PhoneCallDetailsDto.valueOf(phoneCallService.updatePhoneCall(user,
                workflowId,
                meetingId,
                updatePhoneCallDetails));
    }

}
