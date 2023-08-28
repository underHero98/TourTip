package com.example.projectT.domain.listener;




import com.example.projectT.domain.entity.User;
import com.example.projectT.domain.entity.UserHistory;
import com.example.projectT.repository.UserHistoryRepository;
import com.example.projectT.support.BeansUtils;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserEntityListener {
    @PostPersist
    @PostUpdate
    public void prePersistAndPreUpdate(Object o) {

        UserHistoryRepository userHistoryRepository =
                BeansUtils.getBean(UserHistoryRepository.class);

        var newUser = (User) o;

        log.info("prePersistAndPreUpdate user info = {}", newUser);

        var userHistory = UserHistory.builder()
                .user(newUser)
                .nickName(newUser.getNickName())
                .email(newUser.getEmail())
                .pw(newUser.getPw())
                .build();

        userHistoryRepository.save(userHistory);


    }
}
