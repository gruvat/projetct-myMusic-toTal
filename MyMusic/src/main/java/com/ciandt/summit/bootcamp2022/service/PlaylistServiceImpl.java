package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.MusicNotFoundException;
import com.ciandt.summit.bootcamp2022.common.exception.service.PlaylistNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    //@Autowired
    //private PlaylistRepository playlistRepository;

//    @Autowired
//    private MusicRepository musicRepository;
//
//    @Override
//    public Playlist findPlaylistById(String playlistId) {
//        return playlistRepository.findById(playlistId)
//                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with Id "
//                        + playlistId + " not found"));
//    }
//
//    @Override
//    public boolean checkIfMusicNotExists(String musicId) {
//        return musicRepository.findById(musicId).isEmpty();
//    }
//
//    @Override
//    public void addMusicsToPlaylist(Set<Music> musics, String playlistId) {
//        Playlist playlist = findPlaylistById(playlistId);
//
//        musics.stream()
//                .filter(m -> checkIfMusicNotExists(m.getId()))
//                .findAny()
//                .ifPresent(m -> {
//                    throw new MusicNotFoundException("Music with Id " + m.getId() + " not found");
//                });
//
//        playlist.getMusics().addAll(musics);
//        playlistRepository.save(playlist);
//    }

}
