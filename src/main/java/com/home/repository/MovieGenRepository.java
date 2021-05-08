package com.home.repository;

import com.home.entity.MovieGen;
import com.home.entity.MovieGenKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MovieGenRepository extends JpaRepository<MovieGen, MovieGenKey> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM movie_gen m WHERE m.id_movie=:id_movie and m.id_genre=:id_genre", nativeQuery = true)
    void deleteMovieGen(@Param("id_movie") Long id_movie, @Param("id_genre") Long id_genre);
}
