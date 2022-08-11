package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.common.exception.service.InvalidParameterException;
import com.ciandt.summit.bootcamp2022.common.request.RequestMusicsData;
import com.ciandt.summit.bootcamp2022.security.SecurityConfig;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MusicControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MusicService musicService;

    @MockBean
    SecurityConfig securityConfig;

    @InjectMocks
    MusicController musicController;

    @Test
    @DisplayName("Should return all musics if there is no filter")
    public void shouldReturn200WithAllMusics() throws Exception {
        mockMvc.perform(get("/api/musics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }


    @DisplayName(value = "Should return musics that correspond to the filter (ignoring case)")
    @ParameterizedTest(name = "When filtered by {0}")
    @ValueSource(strings= {"Eminem", "Michael Jackson", "LOVE", "BoB dYlaN"})
    public void shouldReturn200WithFilter(String filter) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/musics")
                        .param("filter", filter))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        RequestMusicsData musics = mapper.readValue(result.getResponse().getContentAsString(),
                RequestMusicsData.class);

        musics.getData().forEach(m -> {
            assertTrue(m.getArtist().getName().toLowerCase().contains(filter.toLowerCase())
                    || m.getName().toLowerCase().contains(filter.toLowerCase()));
        });

    }

    @DisplayName(value = "Should return 400 caused by invalid filter")
    @ParameterizedTest(name = "When filtered by {0}")
    @ValueSource(strings= {"A"})
    public void shouldReturn400WhenFilterIsInvalid(String filter) throws Exception {
        mockMvc.perform(get("/api/musics")
                        .param("filter", filter))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof InvalidParameterException))
                .andExpect(result ->
                        assertEquals("The filter must have at least 2 characters.\uD83D\uDD34",
                                result.getResolvedException().getMessage()));
    }

    @DisplayName(value = "Should return 404 caused by invalid path")
    @ParameterizedTest(name = "When path is {0}")
    @ValueSource(strings= {"/api/musicas"})
    public void shouldReturn404WhenPathIsInvalid(String path) throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoHandlerFoundException))
                .andExpect(jsonPath("$.message", is("The given path or resource was not found \uD83D\uDE41")));
    }

    @DisplayName(value = "Should return 204 caused by parameter not found")
    @ParameterizedTest(name = "When parameter is {0}")
    @ValueSource(strings= {"INVALID_PARAMETER"})
    public void shouldReturn204WhenFilterIsNotFound(String filter) throws Exception {
        mockMvc.perform(get("/api/musics")
                        .param("filter", filter))
                .andExpect(status().isNoContent());
    }
}
