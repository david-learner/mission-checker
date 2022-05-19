package com.missionchecker.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class MissionConfiguration {

    private LocalDate missionStartDate;
    private LocalDate missionEndDate;
    private LocalTime missionCheckStartTimeOfDay;
    private LocalTime missionCheckEndTimeOfDay;

    public MissionConfiguration() {
    }

    public MissionConfiguration(LocalDate missionStartDate, LocalDate missionEndDate,
                                LocalTime missionCheckStartTimeOfDay, LocalTime missionCheckEndTimeOfDay) {
        this.missionStartDate = missionStartDate;
        this.missionEndDate = missionEndDate;
        this.missionCheckStartTimeOfDay = missionCheckStartTimeOfDay;
        this.missionCheckEndTimeOfDay = missionCheckEndTimeOfDay;
    }
}
