package com.ge.rfr.actionitem.notes;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ge.rfr.actionitem.notes.model.dto.RfrNotesDto;
import com.ge.rfr.auth.SsoUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/action-items/notes")
@Api(tags = {"RFR-ActionItem-Notes"})
@SwaggerDefinition(tags = {@Tag(name = "RFR-ActionItem-Notes", description = "Operations pertaining to Rfr Action Item Notes")})
public class RfrNotesResource {

    RfrNotesService rfrNotesService;

    @Autowired
    public RfrNotesResource(RfrNotesService rfrNotesService) {
        this.rfrNotesService = rfrNotesService;
    }

    @GetMapping
    @ApiOperation(value = "View a list of Notes Specific to Action Item", response = RfrNotesDto[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public List<RfrNotesDto> getAllNotes(@QueryParam("actionItemId") int actionItemId) {

        return rfrNotesService.getAllNotes(actionItemId)
                .stream()
                .map(RfrNotesDto::valueOf)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ApiOperation(value = "Create a new Action Item Notes")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You are not authorized to create  Action Item Notes"),
    })
    public RfrNotesDto createNote(@ApiIgnore SsoUser user,
                                  @QueryParam("actionItemId") int actionItemId,
                                  @Valid @RequestBody RfrNotesDto rfrNotesDto) {

        return RfrNotesDto.valueOf(rfrNotesService.createNote(user, actionItemId, rfrNotesDto));
    }
}
