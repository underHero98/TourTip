package com.example.projectT.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private Long id;

    private String nickName;
    private String pw;
    private String email;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
