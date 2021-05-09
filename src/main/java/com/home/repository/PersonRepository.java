package com.home.repository;

import com.home.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "SELECT * FROM person p WHERE p.id_person in \n" +
            "(SELECT participate.id_person FROM Participate WHERE id_movie=:id_movie)", nativeQuery = true)
    List<Person> findPersonByMovie(@Param("id_movie") Long id_movie);

    @Query(value = "SELECT * FROM person p WHERE p.id_person in \n" +
            "(SELECT participate.id_person FROM Participate WHERE id_role=:id_role)", nativeQuery = true)
    List<Person> findPersonByRole(@Param("id_role") Long id_role);
}
