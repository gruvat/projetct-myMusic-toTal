package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundInPlaylistException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicsAndArtistsNotFoundException;
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
        log.info("\uD83D\uDCAC  Checking if playlist with Id " + playlistId + " exists");
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with Id "
                        + playlistId + " not found"));
    }

    @Override
    public Music findMusicById(String musicId) {
        log.info("\uD83D\uDCAC  Checking if music with Id " + musicId + " exists");
        return musicRepository.findById(musicId).stream().findFirst()
                .orElseThrow(() -> new MusicNotFoundException("Music with Id "
                + musicId + " not found"));
    }

    @Override
    public void addMusicsToPlaylist(Set<Music> musics, String playlistId) {
        Playlist playlist = findPlaylistById(playlistId);

        musics.forEach(m -> findMusicById(m.getId()));

        playlist.getMusics().addAll(musics);
        log.info("\uD83D\uDFE2️ All musics added to playlist");

        playlistRepository.save(playlist);
        log.info("\uD83D\uDFE2️ Musics saved to playlist");
    }

    @Override
    public Set<Music> findMusicsByPlaylistId(String playlistId) {
        Playlist playlist = findPlaylistById(playlistId);

        Set<Music> musics = musicRepository.findMusicsByPlaylistId(playlist.getId());
        if (musics.isEmpty()) {
            log.info("\uD83D\uDD34  No results for playlist with Id {}", playlistId);
            throw new MusicsAndArtistsNotFoundException();
        }

        log.info("\uD83D\uDFE2️ Successful search");

        return musics;
    }

    @Override
    public Music findMusicInPlaylistByMusicId(Playlist playlist, String musicId) {
        log.info("\uD83D\uDCAC  Checking if music with Id " + musicId +
                " exists in playlist with Id " + playlist.getId());
        return playlist.getMusics().stream().filter(m -> m.getId().equals(musicId))
                .findFirst().orElseThrow(() -> new MusicNotFoundInPlaylistException("Music with id "
                        + musicId + " is not in the playlist."));
    }

    @Override
    public void removeMusicFromPlaylistByMusicId(String playlistId, String musicId) {
        Playlist playlist = findPlaylistById(playlistId);
        Music music = findMusicById(musicId);

        music = findMusicInPlaylistByMusicId(playlist, music.getId());

        playlist.getMusics().remove(music);
        playlistRepository.save(playlist);
        log.info("\uD83D\uDFE2️ Successful removal");
    }

}
