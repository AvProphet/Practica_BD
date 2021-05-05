package com.home.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.util.Date;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue
    private Long idPerson;

    private String name;
    private String secondName;
    private String nationality;
    private Date birthDate;
    private String cityBirth;
    private String countryBirth;
    private File photoPerson;

}
