package com.example.projectT.repository;

import com.example.projectT.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository
    extends JpaRepository<Review, Long> {
    List<Review> findByHotelId(Long id);
    List<Review> findTop6ByHotelId(Long id);
}