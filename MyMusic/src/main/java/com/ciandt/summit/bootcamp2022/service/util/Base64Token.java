package com.ciandt.summit.bootcamp2022.service.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Token {

    private Base64Token() {
    }
    
    public static String encode(String user, String token) {
        String credentials = user + ":" + token;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public static String[] decode(String encodedCredentials) {
        String base64Credentials = encodedCredentials.substring("Basic".length()).trim();
        
        byte[] credentialsDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credentialsDecoded, StandardCharsets.UTF_8);
        String[] arrayWithCredentials = credentials.split(":");
        
        if (arrayWithCredentials.length >= 2) {
            return arrayWithCredentials;
        } else if (arrayWithCredentials.length == 1) {
            return new String[]{arrayWithCredentials[0], ""};
        } else {
            return new String[]{"", ""};
        }
    }
}
