package com.fitable.backend.publicdata.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecommendedExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "SEXDSTN_FLAG_CD")
    private String genderCode;

    @CsvBindByName(column = "TROBL_TY_NM")
    private String troubleType;

    @CsvBindByName(column = "TROBL_GRAD_NM")
    private String troubleGrade;

    @CsvBindByName(column = "SPORTS_STEP_NM")
    private String sportsStep;

    @CsvBindByName(column = "RECOMEND_MVM_NM")
    private String recommendedMovement;

    @CsvBindByName(column = "FLAG_ACCTO_RECOMEND_MVM_RANK_CO")
    @CsvNumber("0")
    private Integer movementRank;
}
