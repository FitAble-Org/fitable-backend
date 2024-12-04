package com.fitable.backend.club.repository;

import com.fitable.backend.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    List<Club> findByCtprvnNmAndTroblTyNm(String ctprvnNm, String troblTyNm);
}