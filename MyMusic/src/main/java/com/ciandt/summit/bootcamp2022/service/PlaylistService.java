package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;

import java.util.Set;

public interface PlaylistService {
    Playlist findPlaylistById(String playlistId);

    Music findMusicById(String musicId);

    void addMusicsToPlaylist(Set<Music> musics, String playlistId);

    Set<Music> findMusicsByPlaylistId(String playlistId);

    Music findMusicInPlaylistByMusicId(Playlist playlist, String musicId);

    void deleteMusicFromPlaylistByMusicId(String playlistId, String musicId);

}
