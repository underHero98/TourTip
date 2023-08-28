package com.example.projectT.repository;

import com.example.projectT.domain.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository
        extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);

    List<Hotel> findByPosXBetweenAndPosYBetween(double x1, double x2, double y1, double y2);



    // 검색쿼리
@Query(value = "select h from Hotel h where h.region= :region and h.checkIn <= :checkIn and h.checkOut >= :checkOut and h.capacity >= :capacity")
    List<Hotel> findHotel(@Param("region") String region,
                               @Param("checkIn") LocalDate checkIn,
                               @Param("checkOut") LocalDate checkOut,
                               @Param("capacity") int capacity);
}

