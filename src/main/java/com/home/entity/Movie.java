package com.home.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue
    private Long id_movie;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    List<Participate> participates;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    List<MovieGen> movieGens;

    private String title;
    private String es_title;
    private LocalDate release_date;
    private String duration;
    private String synopsis;
    private String movie_poster;

    public Movie() {
        participates = new ArrayList<>();
        movieGens = new ArrayList<>();
    }

    public Movie(Long idMovie, String title, String esTitle, LocalDate releaseDate, String duration, String synopsis, String moviePoster) {
        this.id_movie = idMovie;
        this.title = title;
        this.es_title = esTitle;
        this.release_date = releaseDate;
        this.duration = duration;
        this.synopsis = synopsis;
        this.movie_poster = moviePoster;
    }

    public Long getId_movie() {
        return id_movie;
    }

    public void setId_movie(Long idMovie) {
        this.id_movie = idMovie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEs_title() {
        return es_title;
    }

    public void setEs_title(String esTitle) {
        this.es_title = esTitle;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate releaseDate) {
        this.release_date = releaseDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public void setMovie_poster(String moviePoster) {
        this.movie_poster = moviePoster;
    }

    @Override
    public String toString() {
        return title;
    }
}
