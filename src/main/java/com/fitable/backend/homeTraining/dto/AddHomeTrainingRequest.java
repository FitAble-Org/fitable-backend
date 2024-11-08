package com.fitable.backend.homeTraining.dto;

import lombok.Data;

@Data
public class AddHomeTrainingRequest  {
    private String exerciseName;
    private String recommendationLevel;
    private int duration;
}
