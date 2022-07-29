package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.common.exception.service.InvalidParameterException;
import com.ciandt.summit.bootcamp2022.common.exception.service.MusicsAndArtistsNotFoundException;
import com.ciandt.summit.bootcamp2022.entity.Artist;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.repository.ArtistRepository;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public boolean isAValidSearch(String filter) {
        return filter.length() >= 2;
    }

    @Override
    public Set<Music> searchMusicsByFilter(String filter) {

        if(isAValidSearch(filter)){
            Set<Music> musics;

            Set<Artist> foundedArtists = artistRepository.findByNameContainingIgnoreCaseOrderByNameAsc(filter);
            musics = musicRepository.findByNameContainingIgnoreCaseOrderByNameAsc(filter);

            foundedArtists.forEach(a -> musics.addAll(musicRepository.findByArtist(a)));
            if (musics.isEmpty()) {
                throw new MusicsAndArtistsNotFoundException();
            }

            return musics.stream().sorted(Comparator.comparing((Music m) -> m.getArtist().getName().toLowerCase()).thenComparing(Music::getName))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        throw new InvalidParameterException("The filter must have at least 2 characters.");

    }

    @Override
    public Set<Music> searchAllMusics(){

        Set<Music> allMusics = musicRepository.findByNameContainingIgnoreCaseOrderByNameAsc("");

        if(allMusics.isEmpty()){
            throw new MusicsAndArtistsNotFoundException();

        }
        return allMusics.stream().sorted(Comparator.comparing((Music m) -> m.getArtist().getName().toLowerCase()).thenComparing(Music::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
