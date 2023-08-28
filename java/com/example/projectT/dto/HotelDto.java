package com.example.projectT.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HotelDto {

    private Long id;
    private String addr;
    private String theme;
    private String name;
    private String tel;
    private String pk;
    private boolean reserv;
    private String service;
    private String tag;
    private String facilities;
    private double posX;
    private double posY;
    /*
     * 만든거 시작.
     * */
    private LocalDate checkIn;  //체크인원
    private LocalDate checkOut; //체크아웃
    private double starRating;  //별점
    private double price;   //가격
    private int capacity;   // 최대수용인
    private String region;  //지역
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
