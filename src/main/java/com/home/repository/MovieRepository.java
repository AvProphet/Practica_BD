package com.home.repository;

import com.home.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "SELECT * FROM Movie m WHERE m.id_movie in \n" +
            "(SELECT id_movie FROM Participate WHERE id_person=:id_person)", nativeQuery = true)
    List<Movie> findMoviesByPerson(@Param("id_person") Long id_person);
}
