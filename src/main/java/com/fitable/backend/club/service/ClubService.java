package com.fitable.backend.club.service;

import com.fitable.backend.club.dto.ClubResponseDto;
import com.fitable.backend.club.entity.Club;
import com.fitable.backend.club.repository.ClubRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<ClubResponseDto> getClubsByRegionAndDisabilityType(String ctprvnNm, String troblTyNm) {
        List<Club> clubs;

        // 조건별로 데이터 가져오기
        if ((ctprvnNm == null || "전체".equals(ctprvnNm)) && (troblTyNm == null || "전체".equals(troblTyNm))) {
            clubs = clubRepository.findAll(); // 모든 데이터
        } else if (ctprvnNm == null || "전체".equals(ctprvnNm)) {
            clubs = clubRepository.findByTroblTyNm(troblTyNm); // 장애 분류 조건만
        } else if (troblTyNm == null || "전체".equals(troblTyNm)) {
            clubs = clubRepository.findByCtprvnNm(ctprvnNm); // 지역 조건만
        } else {
            clubs = clubRepository.findByCtprvnNmAndTroblTyNm(ctprvnNm, troblTyNm); // 지역 및 장애 분류
        }

        return clubs.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ClubResponseDto convertToDto(Club club) {
        return new ClubResponseDto(
                club.getClubId(),
                club.getItemNm(),
                club.getSubitemNm(),
                club.getCtprvnNm(),
                club.getSignguNm(),
                club.getClubNm(),
                club.getTroblTyNm(),
                club.getOperTimeCn(),
                club.getClubIntrcnCn()
        );
    }
}
