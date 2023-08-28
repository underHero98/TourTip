package com.example.projectT.repository;

import com.example.projectT.dto.ReviewDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTest {

    @Autowired ReviewRepository reviewRepository;

    @Test
    public void initReviewTest() {
    }
}