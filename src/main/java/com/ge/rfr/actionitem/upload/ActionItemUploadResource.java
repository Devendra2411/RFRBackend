package com.ge.rfr.actionitem.upload;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.QueryParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ge.rfr.actionitem.upload.model.dto.ActionItemUploadDto;
import com.ge.rfr.auth.SsoUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/action-items/uploads")
@Api(tags = {"RFR-ActionItem-Uploads"})
@SwaggerDefinition(tags = {@Tag(name = "RFR-ActionItem-Upload",
        description = "Operations pertaining to Rfr Action Item Uploads")})
public class ActionItemUploadResource {

    private ActionItemUploadService uploadService;

    public ActionItemUploadResource(ActionItemUploadService uploadService) {
        this.uploadService = uploadService;

    }

    @PostMapping
    @ApiOperation(value = "Upload files to an Action Item")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You are not authorized to Upload Files")})
    public List<ActionItemUploadDto> uploadActionItemFiles(@ApiIgnore SsoUser user,
                                                           @QueryParam("actionItemId") int actionItemId,
                                                           @RequestParam("files") MultipartFile[] filesList)
            throws IOException {

        return uploadService.uploadActionItemFiles(user, actionItemId, filesList)
                .stream()
                .map(ActionItemUploadDto::valueOf)
                .collect(Collectors.toList());
    }

    @PutMapping
    @ApiOperation(value = "Delete uploaded files from an Action Item")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You are not authorized to delete Files")})
    public List<ActionItemUploadDto> deleteUploadedFiles(@ApiIgnore SsoUser user,
                                                         @QueryParam("actionItemId") int actionItemId,
                                                         @RequestBody List<ActionItemUploadDto> filesList)
            throws IOException {

        return uploadService.deleteUploadedFiles(actionItemId, filesList)
                .stream()
                .map(ActionItemUploadDto::valueOf)
                .collect(Collectors.toList());
    }

}
