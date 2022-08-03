package com.ciandt.summit.bootcamp2022.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.ciandt.summit.bootcamp2022.common.exception.service.CredentialsException;
import com.ciandt.summit.bootcamp2022.common.exception.service.RequestTokenProviderApiException;
import com.ciandt.summit.bootcamp2022.security.Token.TokenDTO;

@Service
public class TokenServiceImpl implements TokenService {

    private final String TOKEN_PROVIDER_URL = "http://localhost:8080/api/v1/token";
    private final String TOKEN_PROVIDER_AUTHENTICATION_PATH = "/authorizer";

    public boolean isAuthorized(HttpServletRequest request) {
        List<String> credentials = getCredentials(request);
        TokenDTO tokenAuthorizerDto = new TokenDTO(credentials.get(0), credentials.get(1)); // name and token

        ResponseEntity<String> responseTokenProvider = getApiAuthenticationResponse(tokenAuthorizerDto);

        return responseTokenProvider.getBody().equals("ok");
    }
    
    private List<String> getCredentials(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
            byte[] credentialsDecoded = Base64.getDecoder().decode(base64Credentials);

            String credentials = new String(credentialsDecoded, StandardCharsets.UTF_8);
            
            String[] credentialsArray = credentials.split(":");
            
            if (credentialsArray.length == 2 && !(credentialsArray[0].isEmpty() || credentialsArray[1].isEmpty())) {
                return List.of(credentialsArray);
            } else {
                throw new CredentialsException("Credentials missing");
            }

        } catch(CredentialsException e) {
            throw e;
        } catch(Exception e) {
            throw new CredentialsException("Credentials not in Basic Auth format");
        }
    }

    private ResponseEntity<String> getApiAuthenticationResponse(TokenDTO tokenDto) {
        try {
            final String URI = this.TOKEN_PROVIDER_URL + this.TOKEN_PROVIDER_AUTHENTICATION_PATH;
            HttpEntity<TokenDTO> bodyRequestTokenApi = new HttpEntity<>(tokenDto);

            return postRequestAndResponseWithString(URI, bodyRequestTokenApi);
        } catch(HttpClientErrorException e) { // 4XX
            throw new CredentialsException("Credentials not authorized");
        } catch(HttpServerErrorException e) { // 5XX
            throw new RequestTokenProviderApiException("Invalid credentials");
        } catch (Exception e) {
            throw new RequestTokenProviderApiException("TokenProvider Api can't be reached");
        }
    }

    private ResponseEntity<String> postRequestAndResponseWithString(String uri, HttpEntity<?> body) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, body, String.class);
    }

}
