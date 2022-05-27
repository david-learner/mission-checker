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

    public static Check of(Member checker, Mission mission, LocalDate executionDate) {
        LocalDateTime executionDatetime = createExecutionDatetime(mission, executionDate);
        mission.validateExecutionDatetime(executionDatetime);
        return new Check(mission, checker, executionDatetime);
    }

    /**
     * 미션 수행 완료 일자(Date)와 현재 시간을 합쳐 미션 수행 완료 일시(Datetime)을 반환한다.
     * @param mission
     * @param missionExecutionDate
     * @return
     */
    private static LocalDateTime createExecutionDatetime(Mission mission, LocalDate missionExecutionDate) {
        if (mission.canCreateCheckWithPastDate()) {
            return LocalDateTime.of(missionExecutionDate, LocalTime.MIN);
        }
        return LocalDateTime.of(missionExecutionDate, LocalTime.now());
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
