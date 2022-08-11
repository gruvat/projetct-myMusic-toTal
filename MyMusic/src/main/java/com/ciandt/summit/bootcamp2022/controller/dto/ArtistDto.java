package com.ciandt.summit.bootcamp2022.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Artist id",
            example = "32844fdd-bb76-4c0a-9627-e34ddc9fd892",
            required = true
    )
    @NotEmpty(message = "Artist must contain id")
    private String id;

    @Schema(description = "Artist name",
            example = "The Swinging Blue Jeans",
            required = true
    )
    @NotEmpty(message = "Artist must contain name")
    private String name;

}
