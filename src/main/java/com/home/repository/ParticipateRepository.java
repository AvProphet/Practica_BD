package com.home.repository;

import com.home.entity.Participate;
import com.home.entity.ParticipateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, ParticipateKey> {

}
