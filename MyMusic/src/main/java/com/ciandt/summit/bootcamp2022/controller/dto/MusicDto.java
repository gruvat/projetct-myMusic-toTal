package com.ciandt.summit.bootcamp2022.controller.dto;

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

}
