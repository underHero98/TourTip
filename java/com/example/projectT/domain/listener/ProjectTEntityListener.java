package com.example.projectT.domain.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ProjectTEntityListener {
    @PrePersist
    public void prePersist(Object object){
        log.info("{}", "ProjectTEntityListener prePersist ----------------");
        if(object instanceof IProjectT) {
            ((IProjectT)object).setCreateAt(LocalDateTime.now());
            ((IProjectT)object).setUpdateAt(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void preUpdate(Object object){
        log.info("{}", "ProjectTEntityListener preUpdate ----------------");
        if(object instanceof IProjectT) {
            ((IProjectT)object).setUpdateAt(LocalDateTime.now());
        }
    }
}
