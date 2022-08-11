package com.ciandt.summit.bootcamp2022.service;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ciandt.summit.bootcamp2022.common.exception.service.CredentialsException;
import com.ciandt.summit.bootcamp2022.common.exception.service.RequestTokenProviderApiException;
import com.ciandt.summit.bootcamp2022.service.util.Base64Token;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;


@MockitoSettings
public class TokenServiceTest {

    @Spy
    private TokenServiceImpl tokenService;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWiremock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
            .port(8080));
    }

    @BeforeEach
    void startingMocking() {
        wireMockServer.start();
        WireMock.reset();
    }

    @AfterEach
    void shuttingDownMocking() {
        wireMockServer.stop();
    }

    @Test
    void callWithValidToken() {

        wireMockServer.givenThat(
            WireMock.post("/api/v1/token/authorizer")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("ok")
                )
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        String validAutorizationHeader = Base64Token.encode("user", "token");

        request.setServerName("localhost:8080");
        request.setRequestURI("/api/musics");
        request.addHeader("Authorization", validAutorizationHeader);

        assertTrue(this.tokenService.isAuthorized(request));
    }

    @Test
    void callWithTokenNotInBasicFormat() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        String credentials = "user:token";

        request.setServerName("localhost:8080");
        request.setRequestURI("/api/musics");
        request.addHeader("Authorization", credentials);

        assertThrowsExactly(CredentialsException.class, 
            () -> this.tokenService.isAuthorized(request));
    }

    @Test
    void callWithTokenMissingCredential() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        String validAutorizationHeader = Base64Token.encode("user", "");

        request.setServerName("localhost:8080");
        request.setRequestURI("/api/musics");
        request.addHeader("Authorization", validAutorizationHeader);

        assertThrowsExactly(CredentialsException.class, 
            () -> this.tokenService.isAuthorized(request));
    }

    @Test
    void callWithTokenMissingBothCredentials() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        String validAutorizationHeader = Base64Token.encode("", "");

        request.setServerName("localhost:8080");
        request.setRequestURI("/api/musics");
        request.addHeader("Authorization", validAutorizationHeader);

        assertThrowsExactly(CredentialsException.class, 
            () -> this.tokenService.isAuthorized(request));
    }

    // erro 400 test
    @Test
    void callWithUnauthorizedCredentials() {

        wireMockServer.givenThat(
            WireMock.post("/api/v1/token/authorizer")
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        String validAutorizationHeader = Base64Token.encode("user", "token");

        request.setServerName("localhost:8080");
        request.setRequestURI("/api/musics");
        request.addHeader("Authorization", validAutorizationHeader);

        assertThrowsExactly(CredentialsException.class, 
            () -> this.tokenService.isAuthorized(request));
    }

    // erro 500 test
    @Test
    void callWithInvalidCredentials() {

        wireMockServer.givenThat(
            WireMock.post("/api/v1/token/authorizer")
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                )
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        String validAutorizationHeader = Base64Token.encode("user", "$%#@%$");

        request.setServerName("localhost:8080");
        request.setRequestURI("/api/musics");
        request.addHeader("Authorization", validAutorizationHeader);

        assertThrowsExactly(RequestTokenProviderApiException.class, 
            () -> this.tokenService.isAuthorized(request));
    }


    // API can't be reached
    @Test
    void callWhenTokenProviderAppIsOffline() {
        wireMockServer.stop();

        MockHttpServletRequest request = new MockHttpServletRequest();
        String validAutorizationHeader = Base64Token.encode("user", "token");

        request.setServerName("localhost:8081");
        request.setRequestURI("/api/musics");
        request.addHeader("Authorization", validAutorizationHeader);

        assertThrowsExactly(RequestTokenProviderApiException.class, 
            () -> this.tokenService.isAuthorized(request));
    }
    
}
