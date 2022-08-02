package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundException;
import com.ciandt.summit.bootcamp2022.common.exception.service.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PlaylistServiceTest {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private MusicRepository musicRepository;

    @DisplayName(value = "Check if playlist exists")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"a39926f4-6acb-4497-884f-d4e5296ef652", "b54c99b2-1816-47d3-b41c-9f1d070462ab"})
    public void testIfPlaylistExistsById(String id) {
        Playlist playlist = playlistService.findPlaylistById(id);
        assertEquals(id, playlist.getId(), () -> "Should return playlist with Id " + id);
    }

    @DisplayName(value = "Check if playlist doesn't exist")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"INVALIDO", "1290"})
    public void testIfPlaylistNotExistsById(String id) {
        PlaylistNotFoundException exception = assertThrows(PlaylistNotFoundException.class,
                () -> playlistService.findPlaylistById(id),
                () -> "Should throw a PlaylistNotFoundException");

        assertEquals("Playlist with Id " + id + " not found",
                exception.getMessage(),
                () -> "Should return exception message");
    }

    @DisplayName(value = "Check if music exists")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"67f5976c-eb1e-404e-8220-2c2a8a23be47", "049364ca-973f-4db9-ae41-9c183d6aa22a"})
    public void testIfMusicExistsById(String id) {
        assertFalse(playlistService.checkIfMusicNotExists(id),
                () -> "Should return false if music exists");
    }

    @DisplayName(value = "Check if music doesn't exist")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"INVALIDO", "5555"})
    public void testIfMusicNotExistsById(String id) {
        assertTrue(playlistService.checkIfMusicNotExists(id),
                () -> "Should return true if music doesn't exist");
    }

    @DisplayName(value = "Add music to playlist")
    @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
    @CsvSource(value = {"05dcdf8c-ae9b-4d25-95c5-293e72db52b9|92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"}
            , delimiter = '|')
    public void testAddOneMusicToPlaylist(String musicId, String playlistId) {
        Set<Music> musics = new HashSet<>();
        musicRepository.findById(musicId).ifPresent(musics::add);

        playlistService.addMusicsToPlaylist(musics, playlistId);

        Playlist playlist = playlistService.findPlaylistById(playlistId);
        assertTrue(playlist.getMusics().containsAll(musics),
                () -> "Should add the music to the playlist");
    }

    @DisplayName(value = "Don't add repeated music")
    @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
    @CsvSource(value = {"c96b8f6f-4049-4e6b-8687-82e29c05b735|92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"}
            , delimiter = '|')
    public void testDontAddRepeatedMusicToPlaylist(String musicId, String playlistId) {
        Set<Music> musics = new HashSet<>();
        musicRepository.findById(musicId).ifPresent(musics::add);
        Playlist playlist = playlistService.findPlaylistById(playlistId);
        int expectedSize = playlist.getMusics().size();

        playlistService.addMusicsToPlaylist(musics, playlistId);

        int actualSize = playlist.getMusics().size();
        assertEquals(expectedSize, actualSize, () -> "Shouldn't add the music");
    }

    @DisplayName(value = "Add multiple musics, excluding repeated ones")
    @ParameterizedTest(name = "To playlistId {0}")
    @ValueSource(strings = "92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed")
    public void testAddMultipleMusicsToOnePlaylistExcludingRepeatedOnes(String playlistId) {
        Set<Music> newMusics = new HashSet<>(List.of(
                musicRepository.getById("05dcdf8c-ae9b-4d25-95c5-293e72db52b9"),
                musicRepository.getById("b4817918-4580-48f6-9467-3493f83fb6ea")));
        Set<Music> repeatedMusics = new HashSet<>(List.of(
                musicRepository.getById("c96b8f6f-4049-4e6b-8687-82e29c05b735"),
                musicRepository.getById("793815a9-93b6-4b46-8763-d157df518511")
        ));

        Set<Music> allMusics = new HashSet<>();
        allMusics.addAll(newMusics);
        allMusics.addAll(repeatedMusics);

        Playlist playlist = playlistService.findPlaylistById(playlistId);

        int expectedSize = playlist.getMusics().size() + newMusics.size();
        playlistService.addMusicsToPlaylist(allMusics, playlistId);
        int actualSize = playlist.getMusics().size();

        assertTrue(playlist.getMusics().containsAll(newMusics), () -> "Should add the new ones");
        assertEquals(expectedSize, actualSize, () -> "Should add only the new ones");
    }

    @DisplayName(value = "Don't add non-existing music")
    @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
    @CsvSource(value = {"blabla|92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"}
            , delimiter = '|')
    public void testDontAddNonExistingMusicToPlaylist(String musicId, String playlistId) {
        Music music = new Music(musicId, "");
        Set<Music> musics = new HashSet<>();
        musics.add(music);

        MusicNotFoundException exception = assertThrows(MusicNotFoundException.class,
                () -> playlistService.addMusicsToPlaylist(musics, playlistId),
                () -> "Should throw a MusicNotFoundException");

        assertEquals("Music with Id " + musicId + " not found",
                exception.getMessage(),
                () -> "Should return exception message");
    }

    @DisplayName(value = "Don't add multiple existent and non-existing musics")
    @ParameterizedTest(name = "to playlistId {0}")
    @ValueSource(strings = {"92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
    public void testDontAddMultipleExistingAndNonExistingMusicsToPlaylist(String playlistId) {
        String nonExistingId = "blabla";

        Set<Music> musics = new HashSet<>(List.of(
                musicRepository.getById("05dcdf8c-ae9b-4d25-95c5-293e72db52b9"),
                new Music(nonExistingId, "")));

        Playlist playlist = playlistService.findPlaylistById(playlistId);
        int expectedSize = playlist.getMusics().size();

        MusicNotFoundException exception = assertThrows(MusicNotFoundException.class,
                () -> playlistService.addMusicsToPlaylist(musics, playlistId),
                () -> "Should throw a MusicNotFoundException");

        int actualSize = playlist.getMusics().size();

        assertEquals(expectedSize, actualSize);

        assertEquals("Music with Id " + nonExistingId + " not found",
                exception.getMessage(),
                () -> "Should return exception message");
    }

    @DisplayName(value = "Check if an exception is thrown when playlist")
    @ParameterizedTest(name = "With Id {1} doesn't exist")
    @CsvSource(value = {"05dcdf8c-ae9b-4d25-95c5-293e72db52b9|blabla"}
            , delimiter = '|')
    public void testDontAddMusicsWhenPlaylistNotExists(String musicId, String playlistId) {
        Set<Music> musics = new HashSet<>(List.of(musicRepository.getById(musicId)));

        PlaylistNotFoundException exception = assertThrows(PlaylistNotFoundException.class,
                () -> playlistService.addMusicsToPlaylist(musics, playlistId),
                () -> "Should throw a PlaylistNotFoundException");

        assertEquals("Playlist with Id " + playlistId + " not found",
                exception.getMessage(),
                () -> "Should return exception message");
    }

}
