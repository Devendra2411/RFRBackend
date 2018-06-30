package com.ge.rfr.actionitem;

import static com.ge.rfr.auth.Privileges.ACTION_ITEM_CREATE;
import static com.ge.rfr.auth.Privileges.ACTION_ITEM_VIEW;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ge.rfr.actionitem.model.dto.RfrActionItemDetailsDto;
import com.ge.rfr.actionitem.model.dto.RfrActionItemDto;
import com.ge.rfr.auth.SsoUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/rfr-workflow/{workflowId}/action-items")
@Api(tags = {"RFR-Action Items"})
@SwaggerDefinition(tags = {@Tag(name = "RFR-Action Items", description = "Operations pertaining to Rfr Action Items")})
public class RfrActionItemsResource {

    private final RfrActionItemsService rfrActionItemsService;

    @Autowired
    public RfrActionItemsResource(RfrActionItemsService rfrActionItemsService) {
        this.rfrActionItemsService = rfrActionItemsService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    @Secured(ACTION_ITEM_VIEW)
    @ApiOperation(value = "View a list of Action Items for the given Workflow Id", response = RfrActionItemDetailsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public RfrActionItemDetailsDto getRfrActionItemsList(@ApiIgnore SsoUser user,
                                                         @PathVariable("workflowId") int workflowId) {
        return rfrActionItemsService.getRfrActionItemsList(user, workflowId);
    }

    @PostMapping
    @Secured(ACTION_ITEM_CREATE)
    @ApiOperation(value = "Create a new Rfr Action Item")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You are not authorized to create a action item"),
    })
    public RfrActionItemDto createRfrActionItem(@ApiIgnore SsoUser user,
                                                @PathVariable("workflowId") int workflowId,
                                                @Valid @RequestBody RfrActionItemDto createActionItemDto) {
        return RfrActionItemDto
                .valueOf(rfrActionItemsService.createRfrActionItem(user, workflowId, createActionItemDto));
    }

    @PutMapping("/{actionItemId}")
    @Secured(ACTION_ITEM_CREATE)
    @ApiOperation(value = "Update Rfr Action Item")
    public RfrActionItemDto updateRfrActionItem(@ApiIgnore SsoUser user,
                                                @PathVariable("actionItemId") int actionItemId,
                                                @Valid @RequestBody RfrActionItemDto updateActionItemDto) {
        return RfrActionItemDto
                .valueOf(rfrActionItemsService.updateRfrActionItem(user, actionItemId, updateActionItemDto));
    }
}
