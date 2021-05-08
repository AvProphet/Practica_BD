package com.home.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue
    private Long id_role;

    @OneToMany(mappedBy = "roles", cascade = CascadeType.REMOVE)
    List<Participate> participates;

    private String desc_role;

    public Roles(Long id_role, String desc_role) {
        this.id_role = id_role;
        this.desc_role = desc_role;
    }

    public Roles() {
        participates = new ArrayList<>();
    }

    public Long getId_role() {
        return id_role;
    }

    public void setId_role(Long id_role) {
        this.id_role = id_role;
    }

    public String getDesc_role() {
        return desc_role;
    }

    public void setDesc_role(String desc_role) {
        this.desc_role = desc_role;
    }

    @Override
    public String toString() {
        return desc_role;
    }
}
