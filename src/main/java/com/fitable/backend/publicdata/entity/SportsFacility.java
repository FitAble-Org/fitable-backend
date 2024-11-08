package com.fitable.backend.publicdata.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SportsFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "FCLTY_NM")
    private String fcltyNm;  // 시설명

    @CsvBindByName(column = "FCLTY_ADDR")
    private String fcltyAddr;  // 시설주소

    @CsvBindByName(column = "ITEM_NM")
    private String itemNm;  // 종목명

    @CsvBindByName(column = "FCLTY_COURSE_SDIV_NM")
    private String fcltyCourseSdivNm;  // 시설 강좌 구분명

    @CsvBindByName(column = "FCLTY_X_CRDNT_VALUE")
    private String fcltyXCrdntValue;  // 시설 X 좌표값

    @CsvBindByName(column = "FCLTY_Y_CRDNT_VALUE")
    private String fcltyYCrdntValue;  // 시설 Y 좌표값
}
