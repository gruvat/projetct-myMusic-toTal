package com.ciandt.summit.bootcamp2022.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ciandt.summit.bootcamp2022.common.exception.service.CredentialsException;
import com.ciandt.summit.bootcamp2022.common.exception.service.RequestTokenProviderApiException;
import com.ciandt.summit.bootcamp2022.security.Token.TokenDTO;
import com.ciandt.summit.bootcamp2022.service.util.Base64Token;

@Log4j2
@Service
public class TokenServiceImpl implements TokenService {

    private final String TOKEN_PROVIDER_URL;
    private final String TOKEN_PROVIDER_ENDPOINTS_PATH = "/api/v1/token";
    private final String TOKEN_PROVIDER_AUTHENTICATION_PATH = "/authorizer";

    public TokenServiceImpl() {
        this.TOKEN_PROVIDER_URL = "http://localhost:8080";
    }

    public TokenServiceImpl(Environment env) {
        this.TOKEN_PROVIDER_URL = env.getProperty("token.url");
    }

    public boolean isAuthorized(HttpServletRequest request) {
        List<String> credentials = getCredentials(request);

        log.info("\uD83D\uDCAC  Getting name and token from request");
        TokenDTO tokenAuthorizerDto = new TokenDTO(credentials.get(0), credentials.get(1));

        ResponseEntity<String> responseTokenProvider = getApiAuthenticationResponse(tokenAuthorizerDto);
        
        log.info("\uD83D\uDFE2️ Auth response successful");

        log.info("\uD83D\uDCAC  Checking if authorization was succeeded");
        return Objects.equals(responseTokenProvider.getBody(), "ok");
    }
    
    private List<String> getCredentials(HttpServletRequest request) {
        try {
            log.info("\uD83D\uDCAC  Getting headers from request");
            String authorizationHeader = request.getHeader("Authorization");
            String[] credentialsArray = Base64Token.decode(authorizationHeader);
            
            if (credentialsArray.length == 2 && !(credentialsArray[0].isEmpty() || credentialsArray[1].isEmpty())) {
                log.info("\uD83D\uDFE2️ Getting credentials from request successfully");
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
            log.info("\uD83D\uDCAC  Getting authorization URI");
            final String URI = getAuthenticationPath();
            HttpEntity<TokenDTO> bodyRequestTokenApi = new HttpEntity<>(tokenDto);

            return postRequestAndResponseWithString(URI, bodyRequestTokenApi);
        } catch(HttpClientErrorException e) {
            throw new CredentialsException("Credentials not authorized");
        } catch(HttpServerErrorException e) {
            throw new RequestTokenProviderApiException("Invalid credentials");
        } catch (Exception e) {
            throw new RequestTokenProviderApiException("TokenProvider Api can't be reached");
        }
    }

    private ResponseEntity<String> postRequestAndResponseWithString(String uri, HttpEntity<?> body) {
        RestTemplate restTemplate = new RestTemplate();
        log.info("\uD83D\uDCAC  Posting request and converting response to String");
        return restTemplate.postForEntity(uri, body, String.class);
    }

    private String getAuthenticationPath() {
        return TOKEN_PROVIDER_URL + TOKEN_PROVIDER_ENDPOINTS_PATH + TOKEN_PROVIDER_AUTHENTICATION_PATH;
    }

}
