package com.home.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue
    private Long id_genre;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE)
    List<MovieGen> movieGens;

    private String desc_genre;

    public Genre(Long id_genre, String desc_genre) {
        this.id_genre = id_genre;
        this.desc_genre = desc_genre;
    }

    public Genre() {
        movieGens = new ArrayList<>();
    }

    public Long getId_genre() {
        return id_genre;
    }

    public void setId_genre(Long id_genre) {
        this.id_genre = id_genre;
    }

    public String getDesc_genre() {
        return desc_genre;
    }

    public void setDesc_genre(String desc_genre) {
        this.desc_genre = desc_genre;
    }

    @Override
    public String toString() {
        return desc_genre;
    }
}
