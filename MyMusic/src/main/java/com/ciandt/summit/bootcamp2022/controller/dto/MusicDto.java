package com.ciandt.summit.bootcamp2022.controller.dto;

import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
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

    @NotEmpty(message = "Music must contain id")
    private String id;

    @NotEmpty(message = "Music must contain name")
    private String name;

    @Valid
    @NotNull(message = "Music must contain artist")
    private ArtistDto artist;

    public static Music toMusic(MusicDto musicDto) {
        Music music = new Music(musicDto.getId(), musicDto.getName());
        music.setArtist(new Artist(musicDto.getArtist().getId(), musicDto.getArtist().getName()));
        return music;
    }

}
