package com.home.service;

import com.home.entity.Role;
import com.home.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class roleDao {

    @Autowired
    private RoleRepository roleRepository;

    public static List<Role> roles = new ArrayList<>();

    private static int userCount = 3;

    static {
        roles.add(new Role(1L, "Main Role"));

        roles.add(new Role(2L, "Secondary Role"));

        roles.add(new Role(3L, "Role"));
    }

    @PostConstruct
    public void Initilalize() {
        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        roleRepository.saveAll(roles);
    }
}
