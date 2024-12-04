package com.fitable.backend.club.controller;

import com.fitable.backend.club.dto.ClubResponseDto;
import com.fitable.backend.club.service.ClubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public List<ClubResponseDto> getAllClubs() {
        return clubService.getAllClubs();
    }

    @GetMapping("/search")
    public List<ClubResponseDto> getClubsByRegionAndDisabilityType(
            @RequestParam String ctprvnNm,
            @RequestParam String troblTyNm) {
        return clubService.getClubsByRegionAndDisabilityType(ctprvnNm, troblTyNm);
    }
}
