package com.home.serverbd.controllers;

import com.home.serverbd.entity.Role;
import com.home.serverbd.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
