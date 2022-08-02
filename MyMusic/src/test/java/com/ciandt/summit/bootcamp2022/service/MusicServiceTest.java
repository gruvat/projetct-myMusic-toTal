package com.ciandt.summit.bootcamp2022.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MusicServiceTest {

    @Autowired
    private MusicService musicService;

    @DisplayName(value = "Valid Parameter")
    @ParameterizedTest(name = "Parameter with more than two characters {0}")
    @ValueSource(strings = {"hi", "Us", "HIGHER", "Sweet Child O' Mine"})
    public void testIfIsAValidParameter(String filter) {

        boolean functionReturn = musicService.isAValidSearch(filter);
        assertTrue(functionReturn);

    }

    @DisplayName(value = "Invalid Parameter")
    @ParameterizedTest(name = "Parameter with less than two characters {0}")
    @ValueSource(strings = {"A", "a", " ", ""})
    public void testIfIsNotAValidParameter(String filter) {

        boolean functionReturn = musicService.isAValidSearch(filter);
        Assertions.assertFalse(functionReturn);

    }

}
