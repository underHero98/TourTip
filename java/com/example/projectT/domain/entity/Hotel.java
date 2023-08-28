package com.example.projectT.domain.entity;

import com.example.projectT.domain.listener.IProjectT;
import com.example.projectT.domain.listener.ProjectTEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({ProjectTEntityListener.class})
public class Hotel implements IProjectT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    * todo: 생성할꺼 별점,가격,체크인날짜,체크아웃날짜,최대수용인원.
    * */

    private String addr;    //주소
    private String theme;   //
    private String name;    //숙소이름
    private String tel;     //숙소전화번호
    private String pk;
    private boolean reserv;     // 예약여부
    private String service;     // 서비스
    private String tag;         // 호텔 태그
    private String facilities;     //시설
    private double posX;        //좌표
    private double posY;        //좌표
    /*
    * 만든거 시작.
    * */
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate checkIn;  //체크인원
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate checkOut; //체크아웃

    private double starRating;  //별점
    private double price;   //가격
    private int capacity;   //최대수용인
    private String region;  //지역

    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private LocalDateTime updateAt;
    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    public void addReview(Review entity){
        this.reviews.add(entity);
    }
}
