package com.example.projectT.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FoodDto {
    private Long id;
    private String addr;
    private String category;
    private String name;
    private String tel;
    private String pk;
    private boolean reserv;
    private String open;
    private String menu;
    private String note;
    private double posX;
    private double posY;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
