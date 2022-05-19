package com.missionchecker.dto;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import com.missionchecker.domain.MissionConfiguration;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public Mission toMission(Member creator) {
        MissionConfiguration configuration = new MissionConfiguration(
                LocalDate.parse(missionStartDate),
                LocalDate.parse(missionEndDate),
                LocalTime.parse(missionCheckStartTimeOfDay),
                LocalTime.parse(missionCheckEndTimeOfDay)
        );
        return Mission.of(creator, name, configuration);
    }
}
