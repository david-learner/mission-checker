package com.missionchecker.dto;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissionCreationRequest {

    private String name;

    public Mission toMission(Member creator) {
        return Mission.of(creator, name);
    }
}
