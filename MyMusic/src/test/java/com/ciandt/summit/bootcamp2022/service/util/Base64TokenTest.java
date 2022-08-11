package com.ciandt.summit.bootcamp2022.service.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;

import org.junit.jupiter.api.Test;

public class Base64TokenTest {

    @Test
    void encodingWithBothCredentials() {
        String encondedCredentials = "Basic " + Base64.getEncoder().encodeToString("user:token".getBytes());
        assertEquals(encondedCredentials, Base64Token.encode("user", "token"));
    }

    @Test
    void encodingWithPartialCredentials() {
        String encondedCredentials = "Basic " + Base64.getEncoder().encodeToString("user:".getBytes());
        assertEquals(encondedCredentials, Base64Token.encode("user", ""));
    }

    @Test
    void encodingWithNoCredentials() {
        String encondedCredentials = "Basic " + Base64.getEncoder().encodeToString(":".getBytes());
        assertEquals(encondedCredentials, Base64Token.encode("", ""));
    }

    @Test
    void decodingWithBothCredentials() {
        String credentials = "user:token";
        String[] decodedCredentials = credentials.split(":");
        String encondedCredentials = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        assertArrayEquals(decodedCredentials, Base64Token.decode(encondedCredentials));
    }

    @Test
    void decodingWithPartialCredentials() {
        String credentials = "user:";
        String encondedCredentials = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        assertArrayEquals(new String[]{"user", ""}, Base64Token.decode(encondedCredentials));
    }

    @Test
    void decodingWithNoCredentials() {
        String credentials = ":";
        String encondedCredentials = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        assertArrayEquals(new String[]{"", ""}, Base64Token.decode(encondedCredentials));
    }
    
}
