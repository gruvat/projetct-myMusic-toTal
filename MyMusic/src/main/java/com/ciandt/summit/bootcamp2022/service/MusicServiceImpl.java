package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.InvalidParameterException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicsAndArtistsNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository musicRepository;

    @Override
    public boolean isAValidSearch(String filter) {
        return filter.length() >= 2;
    }

    @Override
    @Cacheable(value = "music_cache")
    public Set<Music> searchMusicsByFilter(String filter) {

        if(isAValidSearch(filter)){
            Set<Music> musics = musicRepository.findMusicsByMusicsAndArtistsName(filter.toLowerCase());

            if (musics.isEmpty()) {
                throw new MusicsAndArtistsNotFoundException();
            }

            return musics;
        }

        throw new InvalidParameterException("The filter must have at least 2 characters.");

    }

    @Override
    @Cacheable(value = "music_cache")
    public Set<Music> searchAllMusics(){
        Set<Music> allMusics = musicRepository.findMusicsByMusicsAndArtistsName("");

        if(allMusics.isEmpty()){
            throw new MusicsAndArtistsNotFoundException();

        }

        return allMusics;
    }

}
