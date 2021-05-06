package com.home.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovieGenKey implements Serializable {

    @Column(name = "id_genre")
    private Long id_genre;

    @Column(name = "id_movie")
    private Long id_movie;

    public MovieGenKey() {
    }

    public MovieGenKey(Long id_genre, Long id_movie) {
        this.id_genre = id_genre;
        this.id_movie = id_movie;
    }

    public Long getId_genre() {
        return id_genre;
    }

    public void setId_genre(Long id_genre) {
        this.id_genre = id_genre;
    }

    public Long getId_movie() {
        return id_movie;
    }

    public void setId_movie(Long id_movie) {
        this.id_movie = id_movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGenKey that = (MovieGenKey) o;
        return Objects.equals(id_genre, that.id_genre) && Objects.equals(id_movie, that.id_movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_genre, id_movie);
    }
}
