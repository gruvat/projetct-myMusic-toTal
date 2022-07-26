package com.ciandt.summit.bootcamp2022.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "Artistas")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Artista implements Serializable {

    private static final long serialVersionUID = -2346155250576193121L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private String id;

    @Column(name = "Nome")
    private String nome;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artista")
    @JoinColumn(name = "ArtistaId", referencedColumnName = "Id")
    /* alterar o ? para a entidade Musica quando estiver criada */
    private Set<?> musicas = new TreeSet<>();

    public Artista(String nome){
        this.nome = nome;
    }

}