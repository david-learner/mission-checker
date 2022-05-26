package com.missionchecker.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity(name = "CHECKS")
@Getter
public class Check extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Mission mission;
    @ManyToOne
    private Member checker;
    private LocalDateTime completedDatetime;

    protected Check() {
        super(LocalDateTime.now(), false);
    }

    public static Check of(Member checker, Mission mission, LocalDate missionExecutionDate) {
        LocalDateTime completedDatetime;

        if (mission.canRegisterWithPastDate()) {
            completedDatetime = LocalDateTime.of(missionExecutionDate, LocalTime.MIN);
        } else {
            completedDatetime = LocalDateTime.of(missionExecutionDate, LocalTime.now());
        }

        return new Check(mission, checker, completedDatetime);
    }

    private Check(Mission mission, Member checker, LocalDateTime completedDatetime) {
        super(LocalDateTime.now(), false);
        this.mission = mission;
        this.checker = checker;
        this.completedDatetime = completedDatetime;
        addCheckToMission(this);
    }

    protected void addCheckToMission(Check check) {
        mission.getChecks().add(check);
    }
}
