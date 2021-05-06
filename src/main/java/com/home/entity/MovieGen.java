package com.home.entity;

import javax.persistence.*;

@Entity
@Table(name = "movie_gen")
public class MovieGen {

    @EmbeddedId
    private MovieGenKey id;

    @ManyToOne
    @MapsId("id_movie")
    @JoinColumn(name = "id_movie")
    private Movie movie;

    @ManyToOne
    @MapsId("id_genre")
    @JoinColumn(name = "id_genre")
    private Person genre;

    public MovieGen() {
    }

    public MovieGen(MovieGenKey id, Movie movie, Person genre) {
        this.id = id;
        this.movie = movie;
        this.genre = genre;
    }

    public MovieGenKey getId() {
        return id;
    }

    public void setId(MovieGenKey id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Person getGenre() {
        return genre;
    }

    public void setGenre(Person genre) {
        this.genre = genre;
    }
}
