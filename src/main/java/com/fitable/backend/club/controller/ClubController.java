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

    @GetMapping("/search")
    public List<ClubResponseDto> getClubsByRegionAndDisabilityType(
            @RequestParam(required = false, defaultValue = "전체") String ctprvnNm,
            @RequestParam(required = false, defaultValue = "전체") String troblTyNm) {
        return clubService.getClubsByRegionAndDisabilityType(ctprvnNm, troblTyNm);
    }

    // 특정 클럽 반환 API
    @GetMapping("/{id}")
    public ClubResponseDto getClubById(@PathVariable Integer id) {
        return clubService.getClubById(id);
    }
}
