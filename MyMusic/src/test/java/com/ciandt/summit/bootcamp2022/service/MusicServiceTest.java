package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.entity.Music;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MusicServiceTest {

    @Autowired
    private MusicServiceImpl musicService;

    @DisplayName(value = "Check if parameter with more than two characters")
    @ParameterizedTest(name = "{0} is valid")
    @ValueSource(strings = {"hi", "Us", "HIGHER", "Sweet Child O' Mine"})
    public void testIfIsAValidParameter(String filter) {
        boolean functionReturn = musicService.isAValidSearch(filter);
        assertTrue(functionReturn);

    }

    @DisplayName(value = "Check if parameter with less than two characters")
    @ParameterizedTest(name = "{0} is not valid")
    @ValueSource(strings = {"A", "a", " ", ""})
    public void testIfIsNotAValidParameter(String filter) {
        boolean functionReturn = musicService.isAValidSearch(filter);
        Assertions.assertFalse(functionReturn);

    }

    @DisplayName(value = "Find musics by filter for")
    @ParameterizedTest(name = "Parameter {0}")
    @ValueSource(strings = {"ar", "hi", "love"})
    public void testsearchMusicsByFilter(String filter) {
        Set<Music> allMusicsFound = musicService.searchMusicsByFilter(filter);

        allMusicsFound.forEach(m -> assertTrue(m.getName().toLowerCase().contains(filter)
                || m.getArtist().getName().toLowerCase().contains(filter),
                () -> "Should return all musics gathered by music's name or artist's name by filter"));

    }

    @DisplayName(value = "Find musics by filter ordered by artist's name and music's name")
    @Test
    public void testSearchMusicsByFilter() {
        String filter = "love";
        Set<Music> allMusicsFound = musicService.searchMusicsByFilter(filter);

        Set<Music> musicsOrdered = allMusicsFound.stream().sorted(
                Comparator.comparing((Music m) -> m.getArtist().getName().toLowerCase())
                        .thenComparing(Music::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(musicsOrdered, allMusicsFound,
                () -> "Should return musics ordered by music's name and artist's name by filter");
    }

    @DisplayName(value = "Find all musics")
    @Test
    public void searchAllMusics() {
        Set<Music> allMusicsFound = musicService.searchAllMusics();
        assertNotEquals(0, allMusicsFound.size(),
                () -> "Should return at least one music from the database");

    }

    @DisplayName(value = "Find all musics ordered by artist's name and music's name")
    @Test
    public void searchAllMusicsOrderedByArtistNameAndMusicName() {
        Set<Music> allMusicsFound = musicService.searchAllMusics();

        Set<Music> musicsOrdered = allMusicsFound.stream().sorted(
                        Comparator.comparing((Music m) -> m.getArtist().getName().toLowerCase())
                                .thenComparing(Music::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(musicsOrdered, allMusicsFound,
                () -> "Should return all musics ordered by music's name and artist's name");

    }

}
