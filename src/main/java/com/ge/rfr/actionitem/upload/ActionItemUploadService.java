package com.ge.rfr.actionitem.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.ge.rfr.actionitem.RfrActionItemNotFoundException;
import com.ge.rfr.actionitem.RfrActionItemsService;
import com.ge.rfr.actionitem.model.RfrActionItem;
import com.ge.rfr.actionitem.upload.model.ActionItemUpload;
import com.ge.rfr.actionitem.upload.model.dto.ActionItemUploadDto;
import com.ge.rfr.auth.SsoUser;
import com.ge.rfr.boxapi.BoxService;
import com.ge.rfr.boxapi.FolderDetails;
import com.ge.rfr.boxapi.ParentFolderId;
import com.ge.rfr.common.exception.InValidFileUploadedException;
import com.ge.rfr.common.exception.InvalidJsonException;
import com.ge.rfr.common.model.ChangeTracking;
import com.ge.rfr.common.model.User;

@Service
public class ActionItemUploadService {

    private RfrActionItemsService actionItemService;

    private BoxService boxService;

    private ActionItemUploadDao actionItemUploadDao;
    
	private static final String[] FILE_TYPE_WHITELIST = {

			"doc", "docx", "xls", "xlsx", "ppt", "pptx",
			"csv", "txt", "jpeg", "jpg", "bmp", "tiff",
			"png", "svg", "pdf", "zip", "7z"
	};

    @Autowired
    public ActionItemUploadService(ActionItemUploadDao actionItemUploadDao,
                                   BoxService boxService,
                                   RfrActionItemsService actionItemService) {
        this.actionItemUploadDao = actionItemUploadDao;
        this.boxService = boxService;
        this.actionItemService = actionItemService;
    }

    public List<ActionItemUpload> uploadActionItemFiles(SsoUser user, 
    		                                            int actionItemId, 
    		                                            MultipartFile[] filesList) throws IOException {
        // Check the list of files should not contain
        // any file with .exe extension
        for (MultipartFile multipartFile : filesList) {
            String type = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            if (!Arrays.asList(FILE_TYPE_WHITELIST).contains(type))
                throw new InValidFileUploadedException("File Type not supported");
        }

        RfrActionItem actionItem = actionItemService.getRfrActionItemDetails(actionItemId);

        // Check if the ActionItem exists
        if (null == actionItem)
            throw new RfrActionItemNotFoundException(actionItemId);

        // Checks if the box folder with Outage Id already exists
        // If no, it should create folder
        String parentId = boxService.createBoxFolder(
                actionItem.getWorkflow().getSiteName(),
                actionItem.getWorkflow().getEquipSerialNumber(),
                actionItem.getWorkflow().getOutageId()
        );

        // Get the folder items to check if the file with same name already exists
        // If yes, it should replace with new file

        ResponseEntity<Map> folderItemsResponse = boxService.getFolderItems(parentId);

        List<Map<String, Object>> itemEntries = (List<Map<String, Object>>) folderItemsResponse.getBody().get("entries");

        List<ActionItemUpload> uploadedItemList = new ArrayList<>();
        String token = boxService.getTokenForBox();

        for (MultipartFile multipartFile : filesList) {
            ActionItemUpload uploadedItem = new ActionItemUpload();
            boolean isExistingFile = false;
            String existingFileId = "";
            String existingFileETag = "";

            ResponseEntity<Map> response = null;
            for (Map<String, Object> itemEntry : itemEntries) {
                if (multipartFile.getOriginalFilename().equalsIgnoreCase((String) itemEntry.get("name"))) {
                    isExistingFile = true;
                    existingFileId = itemEntry.get("id").toString();
                    existingFileETag = itemEntry.get("etag").toString();
                    break;
                }
            }

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

            // Convert Multipart file to File
            FileSystemResource value = new FileSystemResource(convert(multipartFile));
            // If it is an existing file, upload with new version
            if (isExistingFile) {
                map.add("Zelda", value);
                boxService.updateNewVersionToBox(existingFileId, existingFileETag, map, token);

                // update existing Upload meta-data
                updateExistingUploadDetails(actionItemId, multipartFile, user);

            } else {
                FolderDetails folderDetails = new FolderDetails();
                ParentFolderId parentFolderId = new ParentFolderId();
                parentFolderId.setId(parentId);
                folderDetails.setName(multipartFile.getOriginalFilename());
                folderDetails.setParent(parentFolderId);

                map.add("attributes", folderDetails);
                map.add("file", value);

                response = boxService.uploadFileToBox(map, token);

                List<Map<String, Object>> entries = (List<Map<String, Object>>) response.getBody().get("entries");
                String path = "";

                // If the upload successful, save the file details in action_item_files
                if (!entries.isEmpty()) {
                    path = (String) entries.get(0).get("id");
                    //
                    uploadedItem.setActionItem(actionItem);
                    uploadedItem.setFileName(multipartFile.getOriginalFilename());
                    uploadedItem.setFileId(path);
                    uploadedItem.setFilePath(boxService.getSharedLinkForFile(path, token));
                    uploadedItem.setFileSize(multipartFile.getSize());
                    uploadedItem.setChangeTracking(new ChangeTracking(User.fromSsoUser(user)));

                    uploadedItemList.add(uploadedItem);
                }
            }
        }
        return saveUploadedItems(uploadedItemList, actionItemId);
    }

    public List<ActionItemUpload> deleteUploadedFiles(int actionItemId,
			                                          List<ActionItemUploadDto> deleteFilesList) throws IOException {

		RfrActionItem actionItem = actionItemService.getRfrActionItemDetails(actionItemId);

		// Check if the ActionItem exists
		if (null == actionItem)
			throw new RfrActionItemNotFoundException(actionItemId);

		if (!deleteFilesList.isEmpty()) {
			String token = boxService.getTokenForBox();
			deleteFilesList.forEach(uploadedItem -> {
				boxService.deleteFile(uploadedItem.getFileId(), token);
				actionItemUploadDao.deleteByFileId(uploadedItem.getFileId());
			});

		} else
			throw new InvalidJsonException("Select Atleast 1 File to Delete");
		return getUploadedItems(actionItemId);
	}

    private List<ActionItemUpload> getUploadedItems(int actionItemId) {
        return actionItemUploadDao.findByActionItemId(actionItemId);
    }

    private void updateExistingUploadDetails(int actionItemId, MultipartFile file, SsoUser user) {
        List<ActionItemUpload> existingUploads = getUploadedItems(actionItemId);
        existingUploads.forEach(upload -> {
            if (upload.getFileName().equalsIgnoreCase(file.getOriginalFilename())) {
                upload.getChangeTracking().update(user);
                upload.setFileSize(file.getSize());
                actionItemUploadDao.save(upload);
            }
        });
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        if (convFile.createNewFile()) {
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        }
        return convFile;
    }

    private List<ActionItemUpload> saveUploadedItems(List<ActionItemUpload> list, int actionItemId) {
        if (!list.isEmpty())
            actionItemUploadDao.save(list);
        return getUploadedItems(actionItemId);
    }


}
