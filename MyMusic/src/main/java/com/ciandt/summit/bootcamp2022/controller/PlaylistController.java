package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.common.request.RequestMusicsData;
import com.ciandt.summit.bootcamp2022.common.response.ResponseData;
import com.ciandt.summit.bootcamp2022.controller.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlists")
@Tag(name = "Playlist")
@Log4j2
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping("/{playlistId}/musics")
    @Operation(summary = "Post musics to playlist",
            description = "Post musics to an existing playlist."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<String> postMusicsToPlaylist(@PathVariable final String playlistId,
                                                       @RequestBody @Valid RequestMusicsData musicsData) {

        Set<Music> musics = new HashSet<>();

        musicsData.getData().forEach(musicDto -> {
            Music music = MusicDto.toMusic(musicDto);
            log.info("\uD83D\uDFE2️ MusicDto converted to Music");
            musics.add(music);
        });

        playlistService.addMusicsToPlaylist(musics, playlistId);
        log.info("\uD83D\uDFE2️ Musics added to playlist");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{playlistId}/musics")
    @Operation(summary = "Find musics by playlistId",
            description = "Find all musics in the playlist "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation success"),
            @ApiResponse(responseCode = "204", description = "No results"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<ResponseData<Set<Music>>> getMusicsFromPlaylistId(
            @PathVariable final String playlistId) {
        Set<Music> result;

        result = playlistService.findMusicsByPlaylistId(playlistId);
        log.info("\uD83D\uDFE2️ FindMusics With playlistId {}, total rows: {}", playlistId, result.size());

        return ResponseEntity.ok(ResponseData.of(result));
    }

}
