package com.fitable.backend.hometraining.dto;

import lombok.Data;

@Data
public class RecommendedExerciseResponse {
    private long exerciseId;
    private String exerciseName;
    private int movementRank;
    private String rankName;
    private String sportsStep;

    public RecommendedExerciseResponse(long exerciseId, String exerciseName, int movementRank, String sportsStep) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.movementRank = movementRank;
        this.sportsStep = sportsStep;
        this.rankName = determineRankName(movementRank);
    }

    private String determineRankName(int movementRank) {
        if (movementRank == 1 || movementRank == 2) {
            return "상";
        } else if (movementRank == 3 || movementRank == 4) {
            return "중";
        } else if (movementRank == 5) {
            return "하";
        } else {
            return "알 수 없음";
        }
    }
}
