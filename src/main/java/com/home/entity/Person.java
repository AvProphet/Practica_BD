package com.home.entity;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue
    private Long id_person;

    @OneToMany(mappedBy = "person")
    List<Participate> participates;

    private String name;
    private String second_name;
    private String nationality;
    private LocalDate birth_date;
    private String city_birth;
    private String country_birth;
    private File photo_person;

    public Person() {
        participates = new ArrayList<>();
    }

    public Person(Long idPerson, String name, String secondName, String nationality, LocalDate birthDate, String cityBirth, String countryBirth, File photoPerson) {
        this.id_person = idPerson;
        this.name = name;
        this.second_name = secondName;
        this.nationality = nationality;
        this.birth_date = birthDate;
        this.city_birth = cityBirth;
        this.country_birth = countryBirth;
        this.photo_person = photoPerson;
    }

    public Long getId_person() {
        return id_person;
    }

    public void setId_person(Long idPerson) {
        this.id_person = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String secondName) {
        this.second_name = secondName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birthDate) {
        this.birth_date = birthDate;
    }

    public String getCity_birth() {
        return city_birth;
    }

    public void setCity_birth(String cityBirth) {
        this.city_birth = cityBirth;
    }

    public String getCountry_birth() {
        return country_birth;
    }

    public void setCountry_birth(String countryBirth) {
        this.country_birth = countryBirth;
    }

    public File getPhoto_person() {
        return photo_person;
    }

    public void setPhoto_person(File photoPerson) {
        this.photo_person = photoPerson;
    }

    @Override
    public String toString() {
        return name + " " + second_name;
    }
}
