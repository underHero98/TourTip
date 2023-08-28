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
public class CommentDto {
    private Long id;
    private String content;
    private Long parentsId;
    private Long userId;
    private Long boardId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
