package com.ciandt.summit.bootcamp2022.controller.dto;

import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MusicDto {

    @Schema(description = "Music id",
            example = "67f5976c-eb1e-404e-8220-2c2a8a23be47",
            required = true
    )
    @NotEmpty(message = "Music must contain id")
    private String id;

    @Schema(description = "Music name",
            example = "Hippy Hippy Shake",
            required = true
    )
    @NotEmpty(message = "Music must contain name")
    private String name;

    @Schema(description = "Artist",
            required = true
    )
    @Valid
    @NotNull(message = "Music must contain artist")
    private ArtistDto artist;

    public static Music toMusic(MusicDto musicDto) {
        Music music = new Music(musicDto.getId(), musicDto.getName());
        music.setArtist(new Artist(musicDto.getArtist().getId(), musicDto.getArtist().getName()));
        return music;
    }

}
