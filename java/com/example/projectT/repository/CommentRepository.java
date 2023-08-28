package com.example.projectT.repository;

import com.example.projectT.domain.entity.Board;
import com.example.projectT.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByBoard(Board board);
}
