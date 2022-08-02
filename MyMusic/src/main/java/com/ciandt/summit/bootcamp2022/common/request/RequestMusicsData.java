package com.ciandt.summit.bootcamp2022.common.request;

import com.ciandt.summit.bootcamp2022.controller.dto.MusicDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
public class RequestMusicsData {

    @Valid
    @NotEmpty(message = "Data cannot be empty")
    private Set<MusicDto> data;

}
