package com.example.projectT.service;

import com.example.projectT.domain.entity.Review;
import com.example.projectT.dto.ReviewDto;
import com.example.projectT.repository.HotelRepository;
import com.example.projectT.repository.ReviewRepository;
import com.example.projectT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final HotelService hotelService;
    public void registerReview(ReviewDto reviewDto) {
        var review = dtoToEntity(reviewDto);
        entityToDto(reviewRepository.save(dtoToEntity(reviewDto)));
        hotelService.setStarPoint(reviewDto.getHotelId());
    }

    public Review dtoToEntity(ReviewDto reviewDto) {
        var user = userRepository.findById(reviewDto.getUserId()).get();
        var hotel = hotelRepository.findById(reviewDto.getHotelId()).get();
        var entity = Review.builder()
                .content(reviewDto.getContent())
                .reviewPoint(reviewDto.getReviewPoint())
                .hotel(hotel)
                .user(user)
                .build();
        return entity;
    }

    public ReviewDto entityToDto(Review review) {
        var dto = ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .reviewPoint(review.getReviewPoint())
                .userId(review.getUser().getId())
                .hotelId(review.getHotel().getId())
                .createAt(review.getCreateAt())
                .build();
        return dto;
    }
    public String findNickName(ReviewDto reviewDto){
        return reviewRepository.findById(reviewDto.getId()).get().getUser().getNickName();
    }
}
