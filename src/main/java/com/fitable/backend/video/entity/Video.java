package com.fitable.backend.video.entity;

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
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "category")
    private String category;

    @CsvBindByName(column = "subcategory")
    private String subcategory;

    @CsvBindByName(column = "type")
    private String type;

    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "url")
    private String url;
}
