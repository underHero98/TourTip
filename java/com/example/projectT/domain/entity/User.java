package com.example.projectT.domain.entity;

import com.example.projectT.domain.listener.IProjectT;
import com.example.projectT.domain.listener.ProjectTEntityListener;
import com.example.projectT.domain.listener.UserEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({ProjectTEntityListener.class,UserEntityListener.class})
public class User implements IProjectT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45)
    private String nickName;

    @Column(nullable = false, length = 45)
    private String pw;

    @Column(nullable = false, length = 45)
    private String email;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserHistory> userHistoryEntities = new ArrayList<>();

    public void addHistory(UserHistory entity) {
        this.userHistoryEntities.add(entity);
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @Builder.Default
    private List<Board> Boards = new ArrayList<>();

    public void addBoard(Board entity){
        this.Boards.add(entity);
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment entity){
        this.comments.add(entity);
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    public void addReview(Review entity){
        this.reviews.add(entity);
    }
}
