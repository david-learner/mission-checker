package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "CHECKS")
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
        addCheckToMission(this);
    }

    public static Check of(Member checkedBy, Mission mission) {
        return new Check(mission, checkedBy, LocalDateTime.now());
    }

    protected void addCheckToMission(Check check) {
        mission.getChecks().add(check);
    }
}
