package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.InvalidParameterException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicsAndArtistsNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MusicServiceTest {
    @Autowired
    private MusicService musicService;
    @Autowired
    private MusicRepository musicRepository;

    @DisplayName(value = "Check if valid filter is accepted")
    @ParameterizedTest(name = "filter {0}")
    @ValueSource(strings = {"hi", "Us", "HIGHER", "Sweet Child O' Mine"})
    public void testIfIsAValidParameter(String filter) {
        assertTrue(musicService.isAValidSearch(filter));
    }

    @DisplayName(value = "Check if search throws exception to invalid filter")
    @ParameterizedTest(name = "filter {0}")
    @ValueSource(strings = {"A", "a", " ", ""})
    public void testSearchWithInvalidFilter(String filter) {
        Exception e = assertThrows(InvalidParameterException.class, () -> musicService.searchMusicsByFilter(filter));
        assertEquals("The filter must have at least 2 characters.\uD83D\uDD34", e.getMessage());
    }

    @DisplayName(value = "Check if there are no results for unmatching filter")
    @ParameterizedTest(name = "filter {0}")
    @ValueSource(strings = {"agheurigwheuiwtrh"})
    public void testSearchWithNoMatchingFilter(String filter) {
        assertThrowsExactly(MusicsAndArtistsNotFoundException.class, () -> musicService.searchMusicsByFilter(filter));
    }

    @DisplayName(value = "Check if SearchAllMusics returns all musics")
    @Test
    public void testSearchReturnAllMusics() {
        Set<Music> AllMusicsTest = new HashSet<>(musicRepository.findAll());
        Set<Music> MusicSearchAllTest = musicService.searchAllMusics();
        assertEquals(AllMusicsTest , MusicSearchAllTest);
    }
}
