package com.example.projectT.domain.entity;

import com.example.projectT.domain.listener.IProjectT;
import com.example.projectT.domain.listener.ProjectTEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({ProjectTEntityListener.class})
public class View implements IProjectT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String addr;
    private String theme;
    private String name;
    private String tel;
    private String pk;
    private boolean reserv;
    private String open;
    private String tag;
    private String facilities;
    private String note;
    private double posX;
    private double posY;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private LocalDateTime updateAt;
}
