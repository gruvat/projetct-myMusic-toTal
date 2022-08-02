package com.ciandt.summit.bootcamp2022.service;

import static org.mockito.Mockito.doReturn;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;




@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MusicServiceCachingTest {

    @MockBean
    private MusicRepository mockMusicRepository;

    @Autowired
    @InjectMocks
    private MusicServiceImpl mockedMusicService;

    @Autowired
    private CacheManager cacheManager;

    @AfterEach
    public void resetMocking() {
        Mockito.reset(mockMusicRepository);
        Cache cache = cacheManager.getCache("music_cache");
        cache.clear();
    }

    @Test
    @DisplayName(value = "Search already in cache")
    public void testIfFilterSearchHasCache() {

        String filter = "U2".toLowerCase();
        // Fake returned objects
        Set<Music> fakeMusic = new HashSet<>();
        fakeMusic.add(new Music());
        // Fake returns
        doReturn(fakeMusic).when(mockMusicRepository).findMusicsByMusicsAndArtistsName(filter);
        // First call
        mockedMusicService.searchMusicsByFilter(filter);
        // Second call
        mockedMusicService.searchMusicsByFilter(filter);
        // Verification
        Mockito.verify(mockMusicRepository, Mockito.times(1)).findMusicsByMusicsAndArtistsName(Mockito.any(String.class));

    }

    @Test
    @DisplayName(value = "Makes two searchs without cache")
    public void testIfTwoDifferentFilterSearchesHasNoCache() {

        String filter_1 = "U2".toLowerCase();
        String filter_2 = "Nir".toLowerCase();
        // Fake returned objects
        // -> return 1
        Set<Music> fakeMusicReturn_1 = new HashSet<>();
        fakeMusicReturn_1.add(new Music());
        // -> return 2
        Set<Music> fakeMusicReturn_2 = new HashSet<>();
        fakeMusicReturn_2.add(new Music());
        // Fake returns
        doReturn(fakeMusicReturn_1).when(mockMusicRepository).findMusicsByMusicsAndArtistsName(filter_1);
        doReturn(fakeMusicReturn_2).when(mockMusicRepository).findMusicsByMusicsAndArtistsName(filter_2);
        // First call
        mockedMusicService.searchMusicsByFilter(filter_1);
        // Second call
        mockedMusicService.searchMusicsByFilter(filter_2);
        // Verification
        Mockito.verify(mockMusicRepository, Mockito.times(2)).findMusicsByMusicsAndArtistsName(Mockito.any(String.class));

    }

    @Test
    @DisplayName(value = "Checks if the search for all musics has cache")
    public void testIfSearchAllMusicsHasCache() {

        // Fake returned objects
        Set<Music> fakeMusic = new HashSet<>();
        fakeMusic.add(new Music());
        // Fake returns
        doReturn(fakeMusic).when(mockMusicRepository).findMusicsByMusicsAndArtistsName("");
        // First call
        mockedMusicService.searchAllMusics();
        // Second call
        mockedMusicService.searchAllMusics();
        // Verification
        Mockito.verify(mockMusicRepository, Mockito.times(1)).findMusicsByMusicsAndArtistsName(Mockito.any(String.class));

    }

    @Test
    @DisplayName(value = "Checks if the search for all musics has no cache")
    public void testIfSearchAllMusicsHasNoCache() {
        
        String filter_1 = "U2".toLowerCase();
        // Fake returned objects
        // -> return 1
        Set<Music> fakeMusicReturn_1 = new HashSet<>();
        fakeMusicReturn_1.add(new Music());
        // -> return 2
        Set<Music> fakeMusicReturn_2 = new HashSet<>();
        fakeMusicReturn_2.add(new Music());
        fakeMusicReturn_2.add(new Music());
        // Fake returns
        doReturn(fakeMusicReturn_1).when(mockMusicRepository).findMusicsByMusicsAndArtistsName(filter_1);
        doReturn(fakeMusicReturn_2).when(mockMusicRepository).findMusicsByMusicsAndArtistsName("");
        // First call
        mockedMusicService.searchMusicsByFilter(filter_1);
        // Second call
        mockedMusicService.searchAllMusics();
        // Verification
        Mockito.verify(mockMusicRepository, Mockito.times(2)).findMusicsByMusicsAndArtistsName(Mockito.any(String.class));

    }

}
