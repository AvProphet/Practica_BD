package com.home.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    private Long id_role;

    private String desc_role;

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
        return "Role{" +
                "id_role=" + id_role +
                ", desc_role='" + desc_role + '\'' +
                '}';
    }
}
