package com.ge.rfr.workflow;

import static com.ge.rfr.auth.Privileges.WORKFLOW_CREATE;
import static com.ge.rfr.auth.Privileges.WORKFLOW_VIEW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
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
import com.ge.rfr.fspevent.PgsFspEventService;
import com.ge.rfr.fspevent.model.TrainDataDto;
import com.ge.rfr.workflow.model.RfrWorkflow;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDetailsDto;
import com.ge.rfr.workflow.model.dto.RfrWorkflowDto;
import com.ge.rfr.workflow.model.dto.SerialNumberDetailsDto;
import com.ge.rfr.workflow.model.dto.ValidatorGroups.CalencoDocIdValidator;
import com.ge.rfr.workflow.model.dto.ValidatorGroups.SiteNameValidator;
import com.ge.rfr.workflow.model.dto.Views;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/rfr-workflow")
@Api(tags = {"RFR-Workflow"})
@SwaggerDefinition(tags = {@Tag(name = "RFR-Workflow", description = "Operations that can be performed on Workflow's")})
public class WorkflowResource {

    private final WorkflowService workflowService;

    private final PgsFspEventService pgsFspEventService;

    @Autowired
    public WorkflowResource(WorkflowService workflowService, PgsFspEventService pgsFspEventService) {
        this.workflowService = workflowService;
        this.pgsFspEventService = pgsFspEventService;
    }

    @Transactional(readOnly = true)
    @GetMapping("/sites")
    @Secured(WORKFLOW_CREATE)
    @JsonView(Views.SiteNamesListView.class)
    @ApiOperation(value = "View a list of Site Names", response = SerialNumberDetailsDto[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to access the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public List<RfrWorkflowDetailsDto> getSiteNames(@ApiIgnore SsoUser user) {
        return pgsFspEventService.getDistinctSiteNamesList(user);
    }

    @Transactional(readOnly = true)
    @PostMapping("/getBlocks")
    @Secured(WORKFLOW_CREATE)
    @ApiOperation(value = "View a list of Train IDs for specific Site Name", response = TrainDataDto[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to access the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public List<TrainDataDto> getBlocksForSite(@ApiIgnore SsoUser user,
                                               @Validated(SiteNameValidator.class)
                                               @RequestBody RfrWorkflowDetailsDto rfrWorkflowDetailsDto) throws Exception {
        return pgsFspEventService.getBlocksForSite(user, rfrWorkflowDetailsDto.getSiteName());
    }

    @Transactional(readOnly = true)
    @GetMapping("/{workflowId}")
    @Secured(WORKFLOW_VIEW)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view a workflow"),
            @ApiResponse(code = 400, message = "You are sending bad request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    @ApiOperation(value = "Get details of a specific workflow for a passed workflow id")
    public RfrWorkflowDto getRfrWorkflowDetails(@ApiIgnore SsoUser user,
                                                @PathVariable("workflowId") int workflowId) {
        return RfrWorkflowDto.valueOf(workflowService.getWorkflowDetails(user, workflowId));
    }

    @GetMapping
    @Transactional(readOnly = true)
    @Secured(WORKFLOW_VIEW)
    @ApiOperation(value = "Get a list of Workflow's that are associated with the logged in user",
            response = RfrWorkflowDetailsDto[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to access the resource"),
            @ApiResponse(code = 400, message = "You are sending bad request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    public List<RfrWorkflowDetailsDto> getRfrWorkflowList(@ApiIgnore SsoUser user) {
        return workflowService.getWorkflowList(user);
    }

    @PostMapping
    @Secured(WORKFLOW_CREATE)
    @ApiOperation(value = "Create a new Rfr Workflow", response = RfrWorkflowDto[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to create a workflow"),
            @ApiResponse(code = 400, message = "You are sending bad request to create a workflow"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    public List<RfrWorkflowDto> createRfrWorkflow(@ApiIgnore SsoUser user,
                                                  @Valid @RequestBody List<RfrWorkflowDto> createDtoList) throws IOException {
        List<RfrWorkflow> workflowList = workflowService.createRfrWorkflow(user, createDtoList);
        return workflowList.stream().map(RfrWorkflowDto::valueOf).collect(Collectors.toList());
    }


    @PutMapping("/{workflowId}")
    @Secured(WORKFLOW_CREATE)
    @ApiOperation(value = "Update a Rfr Workflow ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved updated data"),
            @ApiResponse(code = 401, message = "You are not authorized to access the resource"),
            @ApiResponse(code = 400, message = "You are sending a bad request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    public RfrWorkflowDto updateRfrWorkflow(@ApiIgnore SsoUser user,
                                            @PathVariable("workflowId") int workflowId,
                                            @Validated(CalencoDocIdValidator.class) @Valid @RequestBody RfrWorkflowDto updateDto) {
        return RfrWorkflowDto.valueOf(workflowService.updateRfrWorkflow(user, workflowId, updateDto));

    }

}
