package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.InvalidParameterException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicsAndArtistsNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Log4j2
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository musicRepository;

    @Override
    public boolean isAValidSearch(String filter) {

        log.info("\uD83D\uDCAC  Checking if filter length is bigger than 2");

        return filter.length() >= 2;
    }

    @Override
    @Cacheable(value = "music_cache")
    public Set<Music> searchMusicsByFilter(String filter) {

        log.info("\uD83D\uDCAC  Entering music search by filter");

        if(isAValidSearch(filter)){
            Set<Music> musics = musicRepository.findMusicsByMusicsAndArtistsName(filter.toLowerCase());

            if (musics.isEmpty()) {

                log.info("\uD83D\uDD34  No results for filter {}", filter);

                throw new MusicsAndArtistsNotFoundException();
            }

            log.info("\uD83D\uDFE2️ Successful search");

            return musics;
        }

        log.error("\uD83D\uDD34  Filter is invalid");

        throw new InvalidParameterException("The filter must have at least 2 characters.");

    }

    @Override
    @Cacheable(value = "music_cache")
    public Set<Music> searchAllMusics(){

        log.info("\uD83D\uDCAC  Entering music search without filter");

        Set<Music> allMusics = musicRepository.findMusicsByMusicsAndArtistsName("");

        if(allMusics.isEmpty()){

            log.info("\uD83D\uDD34  There are no musics in the database");

            throw new MusicsAndArtistsNotFoundException();

        }

        log.info("\uD83D\uDFE2️ Successful search");

        return allMusics;
    }

}
