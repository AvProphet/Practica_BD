package com.home.repository;

import com.home.entity.Participate;
import com.home.entity.ParticipateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, ParticipateKey> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM participate p WHERE p.id_movie=:id_movie and p.id_role=:id_role and p.id_person=:id_person", nativeQuery = true)
    void deleteParticipate(@Param("id_person") Long id_person, @Param("id_movie") Long id_movie, @Param("id_role") Long id_role);
}
