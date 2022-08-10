package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.InvalidParameterException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicsAndArtistsNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
public class MusicServiceTest {
    @Mock
    private MusicRepository musicRepositoryMocked;
    @InjectMocks
    private MusicServiceImpl musicService;

    @AfterEach
    void resetMocking() {
        Mockito.reset(musicRepositoryMocked);
    }

    @DisplayName(value = "Check if valid filter is accepted")
    @ParameterizedTest(name = "filter {0}")
    @ValueSource(strings = {"hi", "Us", "HIGHER", "Sweet Child O' Mine"})
    void testIfIsAValidParameter(String filter) {
        assertTrue(musicService.isAValidSearch(filter));
    }

    @DisplayName(value = "Check if search throws exception to invalid filter")
    @ParameterizedTest(name = "filter {0}")
    @ValueSource(strings = {"A", "a", " ", ""})
    void testSearchWithInvalidFilter(String filter) {
        //Since the method must throw an exception before using the repository, the mock is not needed
        Exception e = assertThrows(InvalidParameterException.class, () -> musicService.searchMusicsByFilter(filter));
        assertEquals("The filter must have at least 2 characters.\uD83D\uDD34", e.getMessage());
    }

    @DisplayName(value = "Check if throws exception to no matching filter")
    @ParameterizedTest
    @ValueSource(strings = {"teste"})
    void testSearchWithNoMatchingFilter(String filter) {
        when(musicRepositoryMocked.findMusicsByMusicsAndArtistsName(filter)).thenReturn(new HashSet<>());
        assertThrowsExactly(MusicsAndArtistsNotFoundException.class, () -> musicService.searchMusicsByFilter(filter));
    }

    @DisplayName(value = "Check if exception is thrown for empty database")
    @Test
    void testSearchAllMusicsThrowsExceptionWhenEmpty() {
        when(musicRepositoryMocked.findMusicsByMusicsAndArtistsName("")).thenReturn(new HashSet<>());
        assertThrowsExactly(MusicsAndArtistsNotFoundException.class, () -> musicService.searchAllMusics());
    }
    @Nested
    @DisplayName(value = "Testing successful searches")
    class testsSuccessfulSearches {
        private Music musicAndArtistValidMusic;
        private Artist musicAndArtistValidArtist;
        private  Set<Music> musicsFound = new HashSet<>();

        @BeforeEach
        void setUp() {
            musicAndArtistValidMusic = new Music("1", "valid music");
            musicAndArtistValidArtist = new Artist("1", "valid artist");
            musicAndArtistValidMusic.setArtist(musicAndArtistValidArtist);

            musicsFound.add(musicAndArtistValidMusic);
        }

        @DisplayName(value = "Check if search all musics is successful when there are results")
        @Test
        void testSuccessfulSearchAllMusics() {
            when(musicRepositoryMocked.findMusicsByMusicsAndArtistsName("")).thenReturn(musicsFound);
            musicService.searchAllMusics();
            verify(musicRepositoryMocked).findMusicsByMusicsAndArtistsName("");
        }

        @DisplayName(value = "Check if search by filter is successful when there are results")
        @ParameterizedTest
        @ValueSource(strings = {"teste"})
        void testSuccessfulSearchByFilter(String filter) {
            when(musicRepositoryMocked.findMusicsByMusicsAndArtistsName(filter)).thenReturn(musicsFound);
            musicService.searchMusicsByFilter(filter);
            verify(musicRepositoryMocked).findMusicsByMusicsAndArtistsName(filter);
        }
    }
}
