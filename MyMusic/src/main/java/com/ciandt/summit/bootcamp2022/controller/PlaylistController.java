package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.common.exception.dto.ValidationErrorDto;
import com.ciandt.summit.bootcamp2022.common.request.RequestMusicsData;
import com.ciandt.summit.bootcamp2022.common.response.ResponseData;
import com.ciandt.summit.bootcamp2022.controller.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Playlists")
@Log4j2
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping("/{playlistId}/musics")
    @Operation(summary = "Post musics to playlist",
            description = "Post musics to an existing playlist."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Not enough characters", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            }),
            @ApiResponse(responseCode = "401", description = "Not authorized", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            })
    })
    public ResponseEntity<String> postMusicsToPlaylist(@PathVariable @Parameter(example = "a39926f4-6acb-4497-884f-d4e5296ef652") final String playlistId,
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
            @ApiResponse(responseCode = "200", description = "Operation success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MusicDto.class)))
            }),
            @ApiResponse(responseCode = "204", description = "No results", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Not enough characters", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            }),
            @ApiResponse(responseCode = "401", description = "Not authorized", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            })
    })
    public ResponseEntity<ResponseData<Set<Music>>> getMusicsFromPlaylistId(
            @PathVariable @Parameter(example = "a39926f4-6acb-4497-884f-d4e5296ef652") final String playlistId) {
        Set<Music> result;

        result = playlistService.findMusicsByPlaylistId(playlistId);
        log.info("\uD83D\uDFE2️ FindMusics With playlistId {}, total rows: {}", playlistId, result.size());

        return ResponseEntity.ok(ResponseData.of(result));
    }

    @DeleteMapping("/{playlistId}/musics/{musicId}")
    @Operation(summary = "Remove music from playlist",
            description = "Remove music from a playlist"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content, the music was successfully removed",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Not enough characters", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            }),
            @ApiResponse(responseCode = "401", description = "Not authorized", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            })
    })
    public ResponseEntity<String> deleteMusicFromPlaylist(@PathVariable @Parameter(example = "a39926f4-6acb-4497-884f-d4e5296ef652") final String playlistId,
                                                          @PathVariable @Parameter(example = "67f5976c-eb1e-404e-8220-2c2a8a23be47") final String musicId) {

        playlistService.removeMusicFromPlaylistByMusicId(playlistId, musicId);
        log.info("\uD83D\uDFE2️ Music removed from playlist");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
