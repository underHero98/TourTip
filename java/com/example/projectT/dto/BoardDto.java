package com.example.projectT.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private int mode;

    @Builder.Default
    private int hitNum = 0;
    @Builder.Default
    private String qnaCategories = "";

    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
