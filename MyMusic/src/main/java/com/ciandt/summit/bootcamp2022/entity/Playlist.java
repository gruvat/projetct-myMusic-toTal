package com.ciandt.summit.bootcamp2022.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Playlists")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Playlist implements Serializable {

    private static final long serialVersionUID = 8768383472430388424L;

    @Id
    @Column(name = "Id")
    private String id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(
            name = "PlaylistMusicas",
            joinColumns = @JoinColumn(name = "PlaylistId"),
            inverseJoinColumns = @JoinColumn(name = "MusicaId")
    )
    @EqualsAndHashCode.Exclude private Set<Music> musics;

}
