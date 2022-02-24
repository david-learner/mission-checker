package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_CHECK")
@Getter
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Mission mission;
    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member checkedBy;
    private LocalDateTime checkedAt;
}
