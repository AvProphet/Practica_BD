package com.home.entityList;

import com.home.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleList {
    private List<Role> roles;

    public RoleList() {
        roles = new ArrayList<>();
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
