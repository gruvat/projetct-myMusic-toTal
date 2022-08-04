package com.ciandt.summit.bootcamp2022.repository;

import com.ciandt.summit.bootcamp2022.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MusicRepository extends JpaRepository<Music, String> {
    @Query(value = "SELECT * FROM MUSICAS M JOIN ARTISTAS A ON A.ID = M.ARTISTAID WHERE A.NOME " +
            "LIKE '%'|| :name ||'%' OR M.NOME LIKE '%'|| :name ||'%' " +
            "ORDER BY LOWER(A.NOME), LOWER(M.NOME) ASC", nativeQuery = true)
    Set<Music> findMusicsByMusicsAndArtistsName(@Param("name") String name);

    @Query(value = "SELECT * FROM MUSICAS M " +
            "JOIN ARTISTAS A ON A.ID = M.ARTISTAID " +
            "JOIN PLAYLISTMUSICAS P ON P.MUSICAID = M.ID " +
            "WHERE P.PLAYLISTID = :playlistId " +
            "ORDER BY LOWER(A.NOME), LOWER(M.NOME) ASC", nativeQuery = true)
    Set<Music> findMusicsByPlaylistId(@Param("playlistId") String playlistId);
}
