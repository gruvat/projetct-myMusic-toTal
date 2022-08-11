package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.common.exception.dto.ValidationErrorDto;
import com.ciandt.summit.bootcamp2022.common.response.ResponseData;
import com.ciandt.summit.bootcamp2022.controller.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.service.MusicService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/musics")
@Tag(name = "Musics")
@Validated
@RequiredArgsConstructor
@Log4j2
public class MusicController {
    private final MusicService musicService;

    @GetMapping
    @Operation(summary = "Find songs/artists",
               description = "Find songs/artists. The search is not case sensitive. At least 2 characters are required."
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
    public ResponseEntity<ResponseData<Set<Music>>> getMusics (
            @Parameter(
                    name = "filter",
                    allowEmptyValue = true,
                    description = "Filter for search, if you want all musics do not inform it.",
                    example = "Hippy Hippy Shake"
            )
            @RequestParam(name = "filter", required = false) final String filter) {
        Set<Music> result;
        if (filter == null) {
            result = musicService.searchAllMusics();
            log.info("\uD83D\uDFE2️ FindMusics Null filter");
        } else {
            result = musicService.searchMusicsByFilter(filter);
            log.info("\uD83D\uDFE2️ FindMusics With filter {}, total rows: {}", filter, result.size());
        }

        return ResponseEntity.ok(ResponseData.of(result));

    }
}
