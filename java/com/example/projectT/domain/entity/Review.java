package com.example.projectT.domain.entity;

import com.example.projectT.domain.listener.IProjectT;
import com.example.projectT.domain.listener.ProjectTEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({ProjectTEntityListener.class})
public class Review implements IProjectT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private double reviewPoint;
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
