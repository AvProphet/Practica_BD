package com.home.repository;

import com.home.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

    @Query(value = "SELECT * FROM roles r WHERE r.id_role in (SELECT p.id_role FROM Participate p WHERE p.id_movie=:id_movie and p.id_person=:id_person)",
            nativeQuery = true)
    List<Roles> findRolesByMovie(@Param("id_movie") Long id_movie, @Param("id_person") Long id_person);
}
