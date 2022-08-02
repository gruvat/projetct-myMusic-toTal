package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.common.request.RequestMusicsData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/teste")
public class TesteController {  //CONTROLLER PARA TESTE DE VALIDAÇÃO
    @PostMapping
    public String teste(@RequestBody @Valid RequestMusicsData musics) {
        return "ok";
    }
}
