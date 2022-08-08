package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.common.response.ResponseData;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/musics")
@Tag(name = "Music")
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
            @ApiResponse(responseCode = "200", description = "Operation success"),
            @ApiResponse(responseCode = "204", description = "No results"),
            @ApiResponse(responseCode = "400", description = "Not enough characters"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<ResponseData<Set<Music>>> getMusics (
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
