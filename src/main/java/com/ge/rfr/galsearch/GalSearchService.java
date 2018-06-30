package com.ge.rfr.galsearch;

import com.ge.rfr.galsearch.dto.EmployeeDetailsDto;
import com.scalepoint.oauth_token_client.ClientCredentialsGrantTokenClient;
import com.scalepoint.oauth_token_client.ClientSecretCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Service
public class GalSearchService {

    @Value("${oauth.token-info-uri}")
    private String tokenEndPoint;

    @Value("${oauth.clientId}")
    private String clientId;

    @Value("${oauth.client.clientSecret}")
    private String clientPassword;

    @Value("${gal.search.base.url}")
    private String galBaseUrl;

    @Value("${gal.test.url}")
    private String galTestUrl;

    @Value("${gal.field.list}")
    private String galFieldList;

    @Value("${gal.field.seperator}")
    private String galFieldSeperator;

    public List<EmployeeDetailsDto> getEmployeeDetails(String searchText) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        ClientCredentialsGrantTokenClient tokenClient = new ClientCredentialsGrantTokenClient(
                tokenEndPoint,
                new ClientSecretCredentials(clientId, clientPassword)
        );

        String searchFilter = "";
        if (searchText.indexOf('@') != -1 || searchText.indexOf('.') != -1) {
            searchFilter = "mail";
        } else if (searchText.matches("^[0-9]+$")) {
            searchFilter = "uid";
            searchText = searchText + "*";
        } else {
            searchFilter = "cn";
            searchText = "*" + searchText + "*";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenClient.getToken("api"));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(galBaseUrl)
                .queryParam("readData", galTestUrl + searchFilter + galFieldSeperator + searchText + galFieldList);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<EmployeeDetailsDto>> response = // Using UriComponentsBuilder to build/encode the url
                restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<EmployeeDetailsDto>>() {
                });

        return response.getBody();
    }

}
