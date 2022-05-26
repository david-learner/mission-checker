package com.missionchecker.dto;

import com.missionchecker.domain.Check;
import com.missionchecker.domain.MemberRole;
import com.missionchecker.domain.Mission;
import java.util.List;
import lombok.Getter;

@Getter
public class MissionDetailResponse {

    Mission mission;
    MemberRole memberRole;
    List<Check> checks;

    public MissionDetailResponse(Mission mission, MemberRole memberRole, List<Check> checks) {
        this.mission = mission;
        this.memberRole = memberRole;
        this.checks = checks;
    }
}
