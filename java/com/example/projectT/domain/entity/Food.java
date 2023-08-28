package com.example.projectT.domain.entity;

import com.example.projectT.domain.listener.IProjectT;
import com.example.projectT.domain.listener.ProjectTEntityListener;
import com.example.projectT.domain.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({ProjectTEntityListener.class})
public class Food implements IProjectT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addr;
    private String category;
    private String name;
    private String tel;
    private String pk;
    private boolean reserv;
    private String menu;
    private String open;
    private String note;
    private double posX;
    private double posY;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private LocalDateTime updateAt;
}
