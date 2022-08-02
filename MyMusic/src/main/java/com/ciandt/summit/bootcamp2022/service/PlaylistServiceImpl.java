package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundException;
import com.ciandt.summit.bootcamp2022.common.exception.service.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import com.ciandt.summit.bootcamp2022.repository.PlaylistRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Override
    public Playlist findPlaylistById(String playlistId) {
        log.info("Checking if playlist with Id " + playlistId + " exists");
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with Id "
                        + playlistId + " not found"));
    }

    @Override
    public boolean checkIfMusicNotExists(String musicId) {
        log.info("Checking if music with Id " + musicId + " exists");
        return musicRepository.findById(musicId).isEmpty();
    }

    @Override
    public void addMusicsToPlaylist(Set<Music> musics, String playlistId) {
        Playlist playlist = findPlaylistById(playlistId);

        musics.stream()
                .filter(m -> checkIfMusicNotExists(m.getId()))
                .findAny()
                .ifPresent(m -> {
                    throw new MusicNotFoundException("Music with Id " + m.getId() + " not found");
                });


        playlist.getMusics().addAll(musics);
        log.info("All musics added to playlist");

        playlistRepository.save(playlist);
        log.info("Musics saved to playlist");
    }

}
