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

    public List<ClubResponseDto> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ClubResponseDto> getClubsByRegionAndDisabilityType(String ctprvnNm, String troblTyNm) {
        return clubRepository.findByCtprvnNmAndTroblTyNm(ctprvnNm, troblTyNm).stream()
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
