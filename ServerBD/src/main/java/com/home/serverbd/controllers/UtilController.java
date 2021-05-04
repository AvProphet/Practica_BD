package com.home.serverbd.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.serverbd.entity.Role;
import com.home.serverbd.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
@RestController
public class UtilController {

    private RoleRepository roleRepository;

    @Autowired
    public UtilController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    

    @RequestMapping("json")
    public void json() {
        //get json data from resources
        URL urlRoles = this.getClass().getClassLoader().getResource("roles.json");


        if (urlRoles != null ) {
            File jsonRoles = new File(urlRoles.getFile());

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                List<Role> roles = objectMapper.readValue(jsonRoles, new TypeReference<List<Role>>() {
                });

                roleRepository.saveAll(roles);

                log.info("All records saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("url is null");
        }

    }
}
