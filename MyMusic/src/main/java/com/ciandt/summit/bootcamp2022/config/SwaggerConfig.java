package com.ciandt.summit.bootcamp2022.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("MyMusic API")
                        .description(
                        "API for musics and playlists management. There are four endpoints, " +
                                "the first one is for finding musics in the database, the " +
                                "second one is for adding musics in an existing playlist, " +
                                "the third is for adding musics to a playlist and the fourth " +
                                "is for removing a music from a playlist.")
                        .version("0.0.1")
                );
    }
}
