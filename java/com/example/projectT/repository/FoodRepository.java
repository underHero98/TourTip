package com.example.projectT.repository;

import com.example.projectT.domain.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository
        extends JpaRepository<Food, Long> {
    List<Food> findByPosXBetweenAndPosYBetween(double x1, double x2, double y1, double y2);
    Optional<Food> findByName(String name);
}
