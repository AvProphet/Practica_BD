package com.home.entity;

import javax.persistence.*;

@Entity
@Table(name = "participate")
public class Participate {

    @EmbeddedId
    private ParticipateKey id;

    @ManyToOne
    @MapsId("id_movie")
    @JoinColumn(name = "id_movie")
    private Movie movie;

    @ManyToOne
    @MapsId("id_person")
    @JoinColumn(name = "id_person")
    private Person person;

    @ManyToOne
    @MapsId("id_role")
    @JoinColumn(name = "id_role")
    private Roles roles;

    public Participate(ParticipateKey id, Movie movie, Person person, Roles roles) {
        this.id = id;
        this.movie = movie;
        this.person = person;
        this.roles = roles;
    }

    public Participate() {

    }

    public Participate(ParticipateKey participateKey) {
        this.id = participateKey;
    }

    public ParticipateKey getId() {
        return id;
    }

    public void setId(ParticipateKey id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles role) {
        this.roles = role;
    }

    @Override
    public String toString() {
        return "Participate{" +
                "id=" + id +
                ", movie=" + movie +
                ", person=" + person +
                ", role=" + roles +
                '}';
    }
}
