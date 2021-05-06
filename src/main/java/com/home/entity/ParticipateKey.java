package com.home.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipateKey implements Serializable {

    @Column(name = "id_person")
    private Long id_person;

    @Column(name = "id_movie")
    private Long id_movie;

    @Column(name = "id_role")
    private Long id_role;

    public ParticipateKey() {
    }

    public ParticipateKey(Long id_person, Long id_movie, Long id_role) {
        this.id_person = id_person;
        this.id_movie = id_movie;
        this.id_role = id_role;
    }

    public Long getId_person() {
        return id_person;
    }

    public void setId_person(Long id_person) {
        this.id_person = id_person;
    }

    public Long getId_movie() {
        return id_movie;
    }

    public void setId_movie(Long id_movie) {
        this.id_movie = id_movie;
    }

    public Long getId_role() {
        return id_role;
    }

    public void setId_role(Long id_role) {
        this.id_role = id_role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipateKey that = (ParticipateKey) o;
        return Objects.equals(id_person, that.id_person) && Objects.equals(id_movie, that.id_movie) && Objects.equals(id_role, that.id_role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_person, id_movie, id_role);
    }
}
