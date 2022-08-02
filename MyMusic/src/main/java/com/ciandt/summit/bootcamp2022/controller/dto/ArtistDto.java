package com.ciandt.summit.bootcamp2022.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto{

    @NotEmpty(message = "Artist must contain id")
    private String id;

    @NotEmpty(message = "Artist must contain name")
    private String name;

}
