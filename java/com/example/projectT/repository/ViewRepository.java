package com.example.projectT.repository;

import com.example.projectT.domain.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViewRepository
    extends JpaRepository<View, Long> {
    Optional<View> findByName(String name);
    List<View> findByPosXBetweenAndPosYBetween(double x1, double x2, double y1, double y2);
}
