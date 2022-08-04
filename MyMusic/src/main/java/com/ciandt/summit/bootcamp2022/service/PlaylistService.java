package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;

import java.util.Set;

public interface PlaylistService {
    Playlist findPlaylistById(String playlistId);

    boolean checkIfMusicNotExists(String musicId);

    void addMusicsToPlaylist(Set<Music> musics, String playlistId);

    Set<Music> findMusicsByPlaylistId(String playlistId);
}
