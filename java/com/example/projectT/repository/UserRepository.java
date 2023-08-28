package com.example.projectT.repository;

import com.example.projectT.domain.entity.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
    extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
    Optional<User> findByEmailAndPw(String email, String pw);
}
