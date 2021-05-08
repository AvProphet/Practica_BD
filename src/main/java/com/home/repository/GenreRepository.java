package com.home.repository;

import com.home.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(value = "SELECT * FROM genre g WHERE g.id_genre in \n" +
            "(SELECT movie_gen.id_genre FROM movie_gen WHERE id_movie=:id_movie)", nativeQuery = true)
    List<Genre> findGenreByMovie(@Param("id_movie") Long id_movie);
}
