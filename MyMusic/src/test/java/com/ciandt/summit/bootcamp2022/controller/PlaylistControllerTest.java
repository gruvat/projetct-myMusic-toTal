package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundInPlaylistException;
import com.ciandt.summit.bootcamp2022.common.exception.service.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.common.request.RequestMusicsData;
import com.ciandt.summit.bootcamp2022.controller.dto.ArtistDto;
import com.ciandt.summit.bootcamp2022.controller.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.security.SecurityConfig;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PlaylistControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PlaylistService playlistService;

    @MockBean
    SecurityConfig securityConfig;

    @InjectMocks
    MusicController playlistController;

    @Nested
    @DisplayName("Test methods of postMusicsToPlaylist")
    @Transactional
    class testsPostMusicsToPlaylist {

        @DisplayName("Should return 400 when the body is incorrect")
        @ParameterizedTest(name = "With body {0} and playlistId {1}")
        @CsvSource({"INVALID_BODY,92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
        public void shouldReturn400WhenBodyFormatIsInvalid(String body, String playlistId) throws Exception {
            mockMvc.perform(post("/api/playlists/{playlistId}/musics", playlistId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof HttpMessageNotReadableException))
                    .andExpect(jsonPath("$.message", is("Request body is incorrect \uD83D\uDD34")));
        }

        @DisplayName("Should return 400 when attribute is missing")
        @ParameterizedTest(name = "With playlistId {0}")
        @ValueSource(strings = {"92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
        public void shouldReturn400WhenAttributeIsMissing(String playlistId) throws Exception {
            ArtistDto artistDtoWithMissingAttributes = new ArtistDto();
            artistDtoWithMissingAttributes.setId("a2672380-8803-4025-9ad3-4dda587be44c");
            MusicDto musicDto = new MusicDto("4b97d22f-172f-4849-a117-f7cdae0f6c77",
                    "Let The Music Do The Talking", artistDtoWithMissingAttributes);

            Set<MusicDto> musicsData = new HashSet<>(List.of(musicDto));

            mockMvc.perform(post("/api/playlists/{playlistId}/musics", playlistId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(getRequestDataAsString(musicsData)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof MethodArgumentNotValidException))
                    .andExpect(jsonPath("$.message",
                            is("Validation of body request failed \uD83D\uDD34")))
                    .andExpect(jsonPath("$.errors[0]",
                            is("Field: data[].artist.name -  Error: Artist must contain name")));
        }

        @DisplayName("Should return 201 when musics are inserted")
        @ParameterizedTest(name = "With playlistId {0}")
        @ValueSource(strings = {"92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
        public void shouldReturn201WhenRequestIsValid(String playlistId) throws Exception {
            ArtistDto artistDto = new ArtistDto("a2672380-8803-4025-9ad3-4dda587be44c", "Aerosmith");
            MusicDto musicDto = new MusicDto("70138a62-e4e3-4d58-9a36-77bb7d4bbf67",
                    "Let The Music Do The Talking", artistDto);
            MusicDto musicDto2 = new MusicDto("4b97d22f-172f-4849-a117-f7cdae0f6c77",
                    "Adam's Apple", artistDto);

            Set<MusicDto> musicsData = new HashSet<>(List.of(musicDto, musicDto2));

            mockMvc.perform(post("/api/playlists/{playlistId}/musics", playlistId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(getRequestDataAsString(musicsData)))
                    .andExpect(status().isCreated());
        }

        @DisplayName("Should return 400 when the playlist is inexistent")
        @ParameterizedTest(name = "With playlistId {0}")
        @ValueSource(strings = {"INEXISTENT_PLAYLIST"})
        public void shouldReturn400WhenPlaylistIsInexistent(String playlistId) throws Exception {
            ArtistDto artistDto = new ArtistDto("a2672380-8803-4025-9ad3-4dda587be44c", "Aerosmith");
            MusicDto musicDto = new MusicDto("4b97d22f-172f-4849-a117-f7cdae0f6c77",
                    "Let The Music Do The Talking", artistDto);

            Set<MusicDto> musicsData = new HashSet<>(List.of(musicDto));

            mockMvc.perform(post("/api/playlists/{playlistId}/musics", playlistId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(getRequestDataAsString(musicsData)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof PlaylistNotFoundException))
                    .andExpect(result ->
                            assertEquals("Playlist with Id " + playlistId + " not found \uD83D\uDE41",
                                    result.getResolvedException().getMessage()));
        }

        @DisplayName("Should return 400 when the music is inexistent")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource({"INEXISTENT_MUSIC_ID,92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
        public void shouldReturn400WhenMusicIsInexistent(String musicId, String playlistId) throws Exception {
            ArtistDto artistDto = new ArtistDto("a2672380-8803-4025-9ad3-4dda587be44c", "Aerosmith");
            MusicDto musicDto = new MusicDto(musicId,
                    "INEXISTENT_MUSIC", artistDto);

            Set<MusicDto> musicsData = new HashSet<>(List.of(musicDto));

            mockMvc.perform(post("/api/playlists/{playlistId}/musics", playlistId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(getRequestDataAsString(musicsData)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof MusicNotFoundException))
                    .andExpect(result ->
                            assertEquals("Music with Id " + musicId + " not found \uD83D\uDE41",
                                    result.getResolvedException().getMessage()));
        }

        @DisplayName("Should return 400 when on of the music is inexistent")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource({"INEXISTENT_MUSIC_ID,92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
        public void shouldReturn400WhenOneMusicIsExistentAndOneMusicIsInexistent(
                String musicId, String playlistId) throws Exception {
            ArtistDto artistDto = new ArtistDto("a2672380-8803-4025-9ad3-4dda587be44c", "Aerosmith");
            MusicDto musicDtoInexistent = new MusicDto(musicId,
                    "INEXISTENT_MUSIC", artistDto);
            MusicDto musicDto = new MusicDto("4b97d22f-172f-4849-a117-f7cdae0f6c77",
                    "Let The Music Do The Talking", artistDto);

            Set<MusicDto> musicsData = new HashSet<>(List.of(musicDto, musicDtoInexistent));

            mockMvc.perform(post("/api/playlists/{playlistId}/musics", playlistId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(getRequestDataAsString(musicsData)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof MusicNotFoundException))
                    .andExpect(result ->
                            assertEquals("Music with Id " + musicId + " not found \uD83D\uDE41",
                                    result.getResolvedException().getMessage()));
        }

        private String getRequestDataAsString(Set<MusicDto> musicSet) throws JsonProcessingException {
            RequestMusicsData data = new RequestMusicsData();
            data.setData(musicSet);
            return new ObjectMapper().writeValueAsString(data);
        }
    }

    @Nested
    @DisplayName("Test methods of getMusicsFromPlaylistId")
    class testGetMusicsFromPlaylist {

        @DisplayName("Should return 400 when the playlist is inexistent")
        @ParameterizedTest(name = "With playlistId {0}")
        @ValueSource(strings = {"INEXISTENT_PLAYLIST"})
        public void shouldReturn400WhenPlaylistIsInexistent(String playlistId) throws Exception {
            mockMvc.perform(get("/api/playlists/{playlistId}/musics", playlistId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof PlaylistNotFoundException))
                    .andExpect(result ->
                            assertEquals("Playlist with Id " + playlistId + " not found \uD83D\uDE41",
                                    result.getResolvedException().getMessage()));
        }

        @DisplayName(value = "Should return 204 because playlist doesn't have musics")
        @ParameterizedTest(name = "When playlistId is {0}")
        @ValueSource(strings= {"a39926f4-6acb-4497-884f-d4e5296ef653"})
        public void shouldReturn204WhenPlaylistsDontHaveMusics(String playlistId) throws Exception {
            mockMvc.perform(get("/api/playlists/{playlistId}/musics", playlistId))
                    .andExpect(status().isNoContent());
        }

        @DisplayName("Should return 200 when returning the musics from playlist")
        @ParameterizedTest(name = "With playlistId {0}")
        @ValueSource(strings = {"40c9ec48-f6b1-4532-b2bc-93b8c1cc460a"})
        public void shouldReturn200WhenReturningMusicsFromPlaylist(String playlistId) throws Exception {
            mockMvc.perform(get("/api/playlists/{playlistId}/musics", playlistId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andExpect(jsonPath("$.data[0].id", is("11b8903b-6ef4-4e23-b75a-2085ee98ef63")))
                    .andExpect(jsonPath("$.data[1].id", is("6ba583e2-0c21-4059-ac64-51caa336b3a2")));
        }
    }

    @Nested
    @DisplayName("Test methods of deleteMusicsFromPlaylistId")
    @Transactional
    class testDeleteMusicsFromPlaylist {

        @DisplayName("Should return 400 when the playlist is inexistent")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource({"INEXISTENT_PLAYLIST,4b97d22f-172f-4849-a117-f7cdae0f6c77"})
        public void shouldReturn400WhenPlaylistIsInexistent(String musicId, String playlistId) throws Exception {
            mockMvc.perform(delete("/api/playlists/{playlistId}/musics/{musicId}", playlistId, musicId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof PlaylistNotFoundException))
                    .andExpect(result ->
                            assertEquals("Playlist with Id " + playlistId + " not found \uD83D\uDE41",
                                    result.getResolvedException().getMessage()));
        }

        @DisplayName("Should return 400 when the music is inexistent")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource({"INEXISTENT_MUSIC_ID, 40c9ec48-f6b1-4532-b2bc-93b8c1cc460a"})
        public void shouldReturn400WhenMusicIsInexistent(String musicId, String playlistId) throws Exception {
            mockMvc.perform(delete("/api/playlists/{playlistId}/musics/{musicId}", playlistId, musicId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof MusicNotFoundException))
                    .andExpect(result ->
                            assertEquals("Music with Id " + musicId + " not found \uD83D\uDE41",
                                    result.getResolvedException().getMessage()));
        }

        @DisplayName("Should return 400 when the music is not in the playlist")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource({"4b97d22f-172f-4849-a117-f7cdae0f6c77, 40c9ec48-f6b1-4532-b2bc-93b8c1cc460a"})
        public void shouldReturn400WhenMusicIsNotInThePlaylist(String musicId, String playlistId) throws Exception {
            mockMvc.perform(delete("/api/playlists/{playlistId}/musics/{musicId}", playlistId, musicId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof MusicNotFoundInPlaylistException))
                    .andExpect(result ->
                            assertEquals("Music with Id " + musicId + " is not in the playlist. \uD83D\uDE41",
                                    result.getResolvedException().getMessage()));
        }

        @DisplayName("Should return 204 when the music is deleted from playlist")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource({"11b8903b-6ef4-4e23-b75a-2085ee98ef63, 40c9ec48-f6b1-4532-b2bc-93b8c1cc460a"})
        public void shouldReturn204WhenMusicIsDeletedFromPlaylist(String musicId, String playlistId) throws Exception {
            mockMvc.perform(get("/api/playlists/{playlistId}/musics", playlistId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(2)));
            mockMvc.perform(delete("/api/playlists/{playlistId}/musics/{musicId}", playlistId, musicId))
                    .andExpect(status().isNoContent());
            mockMvc.perform(get("/api/playlists/{playlistId}/musics", playlistId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(1)));
        }
    }

}

