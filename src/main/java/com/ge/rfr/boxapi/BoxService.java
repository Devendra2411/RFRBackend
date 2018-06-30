package com.ge.rfr.boxapi;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.rfr.common.exception.FileNotFoundException;
import com.ge.rfr.common.exception.RestTemplateResponseErrorHandler;
import com.scalepoint.oauth_token_client.ClientCredentialsGrantTokenClient;
import com.scalepoint.oauth_token_client.ClientSecretCredentials;

@Service
public class BoxService {

	private static final String STR_AUTHORIZATION = "Authorization";
	private static final String STR_BEARER = "Bearer ";
	HttpHeaders headers = new HttpHeaders();
	@Value("${oauth.token-stage-info-uri}")
	private String tokenEndPoint;
	@Value("${oauth.stageClientId}")
	private String clientId;
	@Value("${oauth.client.stageClientSecret}")
	private String clientPassword;
	@Value("${box.base.url}")
	private String boxBaseUrl;
	@Value("${copy.file.url}")
	private String copyFileUrl;
	@Value("${email}")
	private String email;
	@Value("${get.folder.path}")
	private String getFolder;
	@Value("${upload.file.path}")
	private String uploadFile;
	@Value("${delete.file.path}")
	private String deleteFile;
	@Value("${upload.new.version.path}")
	private String uploadNewVersion;
	@Value("${parent.id}")
	private String parentId;
	
	private String accessToken;
	private RestTemplate restTemplate;

	@Autowired
	public BoxService(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

	public String getTokenForBox() throws IOException {

		ClientCredentialsGrantTokenClient tokenClient = new ClientCredentialsGrantTokenClient(tokenEndPoint,
				new ClientSecretCredentials(clientId, clientPassword));

		headers.set(STR_AUTHORIZATION, STR_BEARER + tokenClient.getToken("api"));

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(boxBaseUrl).queryParam("email", email);

		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,
				entity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		BoxTokenResponseVO tokenResponse = objectMapper.readValue(response.getBody(), BoxTokenResponseVO.class);
		accessToken=tokenResponse.getAccessToken();
		return accessToken;
	}

	public String createBoxFolder(String siteName, String esn, int outageId) throws IOException {
		
		String folderName = siteName + "-" + esn + "-" + Integer.toString(outageId);
		getTokenForBox();

		headers.set(STR_AUTHORIZATION, STR_BEARER + accessToken);
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getFolder);

		FolderDetails folderDetails = new FolderDetails();
		ParentFolderId parentFolderId = new ParentFolderId();
		parentFolderId.setId(parentId);

		folderDetails.setName(folderName);
		folderDetails.setParent(parentFolderId);

		HttpEntity<?> createFolderEntity = new HttpEntity<>(folderDetails, headers);
		ResponseEntity<Map> createdFolderResponse = restTemplate.exchange(uriBuilder.build().encode().toUri(),
				HttpMethod.POST, createFolderEntity, Map.class);

		return (String) createdFolderResponse.getBody().get("id");
	}

	public String getCalencoDocId(String fileName) throws IOException {

		headers.set(STR_AUTHORIZATION, STR_BEARER + accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getFolder + "/" + parentId + "/items")
				.queryParam("fields", "name,tags");
		ResponseEntity<Map> getFileResponse = restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET,
				entity, Map.class);
		List<Map> entries = (List<Map>) getFileResponse.getBody().get("entries");
		entries = entries.stream()
				.filter(obj -> obj.get("type").equals("file")
						&& ((String) obj.get("name")).split("\\.(?=[^\\.]+$)")[0].equalsIgnoreCase(fileName))
				.collect(Collectors.toList());
	
		if (entries.isEmpty()) {
			throw new FileNotFoundException("File not found with name " + fileName);
		}
		return (String) entries.get(0).get("id");
	}

	public String copyCalencoDoc(String sourceId, String destinationId) throws IOException {
		System.out.println(sourceId + "-" + destinationId);
		String token = getTokenForBox();
		headers.set(STR_AUTHORIZATION, STR_BEARER + token);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(copyFileUrl + "/" + sourceId + "/copy");

		FolderDetails folderDetails = new FolderDetails();
		ParentFolderId parentFolderId = new ParentFolderId();
		parentFolderId.setId(destinationId);
		folderDetails.setParent(parentFolderId);

		HttpEntity<?> entity = new HttpEntity<>(folderDetails, headers);

		ResponseEntity<Map> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity,
				Map.class);

		
		return (String) response.getBody().get("id");
	}

	public ResponseEntity<Map> getFolderItems(String parentId) throws IOException {
		headers.set(STR_AUTHORIZATION, STR_BEARER + getTokenForBox());
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getFolder + "/" + parentId + "/items");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		return restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, Map.class);
	}

	public ResponseEntity<Map> uploadFileToBox(MultiValueMap<String, Object> map, String token) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uploadFile);
		HttpHeaders header = new HttpHeaders();
		header.set(STR_AUTHORIZATION, STR_BEARER + token);
		header.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<?> entity = new HttpEntity<>(map, header);

		return restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.POST, entity, Map.class);
	}

	public ResponseEntity<Map> updateNewVersionToBox(String fileId, String eTag, MultiValueMap<String, Object> map,
			String token) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uploadNewVersion + fileId + "/content");
		HttpHeaders header = new HttpHeaders();
		header.set(STR_AUTHORIZATION, STR_BEARER + token);
		header.set("If-Match", eTag);
		header.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<?> entity = new HttpEntity<>(map, header);

		return restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.POST, entity, Map.class);
	}

	public ResponseEntity<Map> deleteFile(String filePath, String token) {
		// Get the file tags first

		ResponseEntity<Map> tagsResponse = getFileTags(filePath, token);

		if (200 == getFileTags(filePath, token).getStatusCodeValue()) {
			String eTag = tagsResponse.getBody().get("etag").toString();

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(deleteFile + filePath);
			HttpHeaders header = new HttpHeaders();
			header.set(STR_AUTHORIZATION, STR_BEARER + token);
			header.set("If-Match", eTag);
			HttpEntity<?> entity = new HttpEntity<>(header);

			return restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.DELETE, entity, Map.class);
		} else
			throw new FileNotFoundException();
	}

	private ResponseEntity<Map> getFileTags(String filePath, String token) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(deleteFile + filePath + "?fields=tags");
		HttpHeaders header = new HttpHeaders();
		header.set(STR_AUTHORIZATION, STR_BEARER + token);

		HttpEntity<?> entity = new HttpEntity<>(header);

		return restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, entity, Map.class);
	}

	public String getSharedLinkForFile(String fileId, String token) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(deleteFile + fileId);
		HttpHeaders header = new HttpHeaders();
		header.set(STR_AUTHORIZATION, STR_BEARER + token);
		LinkDetailsWrapper wrapper = new LinkDetailsWrapper();
		LinkAccessDetails details = new LinkAccessDetails();
		details.setAccess("Open");
		wrapper.setShared_link(details);
		HttpEntity<?> entity = new HttpEntity<>(wrapper, header);

		ResponseEntity<Map> sharedLinkResponse = restTemplate.exchange(uriBuilder.build().encode().toUri(),
				HttpMethod.PUT, entity, Map.class);
		Map<String, Object> sharedLinkMap = (Map<String, Object>) sharedLinkResponse.getBody().get("shared_link");

		return (String) sharedLinkMap.get("download_url");
	}

}
