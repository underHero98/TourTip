package com.example.projectT.repository;

import com.example.projectT.domain.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository
        extends JpaRepository<UserHistory, Long> {

}
