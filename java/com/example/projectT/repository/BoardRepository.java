package com.example.projectT.repository;

import com.example.projectT.domain.entity.Board;
import com.example.projectT.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository
    extends JpaRepository<Board, Long> {
	Page<Board> findByMode(int mode, Pageable pageable);
	List<Board> findAllByMode(int mode);
    Page<Board> findByModeAndContentContaining(int mode, String keyword, Pageable pageable);
    Page<Board> findByModeAndTitleContaining(int mode, String keyword, Pageable pageable);
	@Query(value = "select b from Board b join User u on b.user.id = u.id where u.nickName like :keyword and b.mode = :mode")
	Page<Board> findByUserNickNameContaining(
			@Param("mode") int mode,
			@Param("keyword") String keyword,
			Pageable pageable);
}
