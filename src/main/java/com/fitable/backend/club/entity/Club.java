package com.fitable.backend.club.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "club")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "ITEM_NM")
    private String itemNm;

    @Column(name = "SUBITEM_NM")
    private String subitemNm;

    @Column(name = "CTPRVN_NM")
    private String ctprvnNm;

    @Column(name = "SIGNGU_NM")
    private String signguNm;

    @Column(name = "CLUB_NM")
    private String clubNm;

    @Column(name = "TROBL_TY_NM")
    private String troblTyNm;

    @Column(name = "OPER_TIME_CN")
    private String operTimeCn;

    @Column(name = "CLUB_INTRCN_CN", columnDefinition = "TEXT")
    private String clubIntrcnCn;
}

