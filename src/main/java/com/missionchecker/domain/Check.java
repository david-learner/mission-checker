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
    private Member checker;
    private LocalDateTime checkedAt;

    public Check() {
    }

    private Check(Mission mission, Member checker, LocalDateTime checkedAt) {
        this.mission = mission;
        this.checker = checker;
        this.checkedAt = checkedAt;
        addToMission();
    }

    public static Check of(Member checkedBy, Mission mission) {
        return new Check(mission, checkedBy, LocalDateTime.now());
    }

    private void addToMission() {
        mission.getChecks().add(this);
    }
}
