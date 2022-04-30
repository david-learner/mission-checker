package com.missionchecker.dto;

import com.missionchecker.domain.MemberRole;
import com.missionchecker.domain.Mission;
import lombok.Getter;

@Getter
public class MissionDetailResponse {

    Mission mission;
    MemberRole memberRole;

    public MissionDetailResponse(Mission mission, MemberRole memberRole) {
        this.mission = mission;
        this.memberRole = memberRole;
    }
}
