package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    private Check(Member checker, LocalDateTime checkedAt) {
        this.checker = checker;
        this.checkedAt = checkedAt;

    }

    public static Check of(Member checkedBy, Mission mission) {
        Check check = new Check(checkedBy, LocalDateTime.now());
        check.addCheckToMission(check, mission);
        return check;
    }

    protected void addCheckToMission(Check check, Mission mission) {
        mission.getChecks().add(check);
    }
}
