package com.missionchecker.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createdAt;
    // todo boolean to bit converter 적용
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted;

    public BaseEntity(LocalDateTime createdAt, Boolean isDeleted) {
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }
}
