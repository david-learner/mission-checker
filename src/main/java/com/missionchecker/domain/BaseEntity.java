package com.missionchecker.domain;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createdAt;
    private Boolean isDeleted;
}
