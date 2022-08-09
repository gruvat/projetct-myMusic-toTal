package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundException;
import com.ciandt.summit.bootcamp2022.common.exception.service.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@MockitoSettings
public class PlaylistServiceTest {
    @Mock
    private MusicRepository musicRepositoryMocked;

    @Mock
    private PlaylistRepository playlistRepositoryMocked;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @DisplayName(value = "Check if findPlaylistById search by id")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"1", "2"})
    public void testIfPlaylistExistsById(String id) {
        when(playlistRepositoryMocked.findById(id)).thenReturn(Optional.of(new Playlist()));

        playlistService.findPlaylistById(id);
        verify(playlistRepositoryMocked, times(1)).findById(id);
    }

    @DisplayName(value = "Check if findPlaylistById throws exception when there is no result")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"notExistent"})
    public void testIfPlaylistNotExistsById(String id) {
        Exception e = assertThrowsExactly(PlaylistNotFoundException.class,
                () -> playlistService.findPlaylistById(id));
        assertEquals("Playlist with Id " + id + " not found \uD83D\uDE41", e.getMessage());
    }

    @DisplayName(value = "Check if method checkIfMusicNotExists returns false when music exists")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"1", "2"})
    public void testIfMusicExistsById(String id) {
        when(musicRepositoryMocked.findById(id)).thenReturn(Optional.of(new Music()));
        assertFalse(playlistService.checkIfMusicNotExists(id));
    }

    @DisplayName(value = "Check if method checkIfMusicNotExists returns true when music does not exist")
    @ParameterizedTest(name = "With Id {0}")
    @ValueSource(strings = {"notExistent"})
    public void testIfMusicNotExistsById(String id) {
        assertTrue(playlistService.checkIfMusicNotExists(id));
    }

    @Nested
    @DisplayName(value = "Testing addMusicsToPlaylist method")
    class testMethodAddMusicsToPlaylist {
        private Music musicAndArtistValidMusic;
        private Artist musicAndArtistValidArtist;
        private Set<Music> musics;
        private Playlist playlistEmpty;

        @BeforeEach
        void setUp() {
            musicAndArtistValidMusic = new Music("1","valid music");
            musicAndArtistValidArtist = new Artist("1", "valid artist");
            musicAndArtistValidMusic.setArtist(musicAndArtistValidArtist);

            musics = new HashSet<>(Arrays.asList(musicAndArtistValidMusic));

            playlistEmpty = new Playlist("1");
            playlistEmpty.setMusics(new HashSet<>());
        }

        @DisplayName(value = "Add music to playlist")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource(value = {"1|1"}, delimiter = '|')
        public void testAddOneMusicToPlaylist(String musicId, String playlistId) {
            when(musicRepositoryMocked.findById(musicId)).thenReturn(Optional.of(musicAndArtistValidMusic));
            when(playlistRepositoryMocked.findById(playlistId)).thenReturn(Optional.of(playlistEmpty));

            Playlist playlistExpected = new Playlist("1");
            playlistExpected.setMusics(new HashSet<>(Arrays.asList(musicAndArtistValidMusic)));

            playlistService.addMusicsToPlaylist(musics, playlistId);

            ArgumentCaptor<Playlist> playlistArgumentCaptor = ArgumentCaptor.forClass(Playlist.class);

            verify(playlistRepositoryMocked).save(playlistArgumentCaptor.capture());

            Playlist playlistCaptured = playlistArgumentCaptor.getValue();

            assertThat(playlistCaptured).isEqualTo(playlistExpected);
            assertThat(playlistCaptured.getMusics()).isEqualTo(playlistExpected.getMusics());
        }

        @DisplayName(value = "Don't add non-existing music")
        @ParameterizedTest(name = "With musicId {0} and playlistId {1}")
        @CsvSource(value = {"1|1"} , delimiter = '|')
        public void testDontAddNonExistingMusicToPlaylist(String musicId, String playlistId) {
            when(musicRepositoryMocked.findById(anyString())).thenReturn(Optional.empty());
            when(playlistRepositoryMocked.findById(playlistId)).thenReturn(Optional.of(playlistEmpty));

            Exception e = assertThrowsExactly(MusicNotFoundException.class,
                    () -> playlistService.addMusicsToPlaylist(musics, playlistId));

            assertEquals("Music with Id " + musicId + " not found \uD83D\uDE41", e.getMessage());
        }

        @DisplayName(value = "Don't add multiple existent and non-existing musics")
        @ParameterizedTest(name = "to playlistId {0}")
        @ValueSource(strings = {"1"})
        public void testDontAddMultipleExistingAndNonExistingMusicsToPlaylist(String playlistId) {
            when(musicRepositoryMocked.findById("1")).thenReturn(Optional.of(new Music()));
            when(musicRepositoryMocked.findById("2")).thenReturn(Optional.empty());
            when(playlistRepositoryMocked.findById(playlistId)).thenReturn(Optional.of(playlistEmpty));

            musics.add(new Music("2", "not existent"));

            Exception e = assertThrowsExactly(MusicNotFoundException.class,
                    () -> playlistService.addMusicsToPlaylist(musics, playlistId));

            verify(playlistRepositoryMocked, times(0)).save(playlistEmpty);
            assertTrue(playlistEmpty.getMusics().isEmpty());
        }

        @DisplayName(value = "Check if an exception is thrown when playlist")
        @ParameterizedTest(name = "With Id {1} doesn't exist")
        @CsvSource(value = {"1|notExistent"}, delimiter = '|')
        public void testDontAddMusicsWhenPlaylistNotExists(String musicId, String playlistId) {
            when(playlistRepositoryMocked.findById(playlistId)).thenReturn(Optional.empty());

            Exception e = assertThrowsExactly(PlaylistNotFoundException.class,
                    () -> playlistService.addMusicsToPlaylist(musics, playlistId));

            assertEquals("Playlist with Id " + playlistId + " not found \uD83D\uDE41",
                    e.getMessage());
        }
    }

}
