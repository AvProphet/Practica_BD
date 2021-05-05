package com.home.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.util.Date;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue
    private Long idMovie;

    private String title;
    private String esTitle;
    private Date releaseDate;
    private String duration;
    private String synopsis;
    private File moviePoster;
}
