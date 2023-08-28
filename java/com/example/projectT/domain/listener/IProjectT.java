package com.example.projectT.domain.listener;

import java.time.LocalDateTime;

public interface IProjectT {

    LocalDateTime getCreateAt();
    LocalDateTime getUpdateAt();
    void setCreateAt(LocalDateTime createAt);
    void setUpdateAt(LocalDateTime updateAt);
}
