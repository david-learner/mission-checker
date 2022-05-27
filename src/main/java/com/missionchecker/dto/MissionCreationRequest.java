package com.missionchecker.dto;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import com.missionchecker.domain.MissionConfiguration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MissionCreationRequest {

    private String name;
    private String missionStartDate;
    private String missionEndDate;
    private String missionCheckStartTimeOfDay;
    private String missionCheckEndTimeOfDay;
    private boolean canCreateCheckWithPastDate;

    public Mission toMission(Member creator) {
        LocalTime startTime = LocalTime.MIN;
        LocalTime endTime = LocalTime.MAX;

        if (!Objects.isNull(missionCheckStartTimeOfDay)) {
            startTime = LocalTime.parse(missionCheckStartTimeOfDay);
        }
        if (!Objects.isNull(missionCheckEndTimeOfDay)) {
            endTime = LocalTime.parse(missionCheckEndTimeOfDay);
        }

        MissionConfiguration configuration = new MissionConfiguration(
                LocalDate.parse(missionStartDate), LocalDate.parse(missionEndDate), startTime, endTime,
                canCreateCheckWithPastDate
        );

        return Mission.of(creator, name, configuration);
    }
}
