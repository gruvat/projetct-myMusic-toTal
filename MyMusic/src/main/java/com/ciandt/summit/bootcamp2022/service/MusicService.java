package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.entity.Music;

import java.util.Set;

public interface MusicService {

    boolean isAValidSearch(String filter);

    Set<Music> searchMusicsByFilter(String filter);

    Set<Music> searchAllMusics();

}