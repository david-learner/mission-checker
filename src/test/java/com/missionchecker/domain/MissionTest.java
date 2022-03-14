package com.missionchecker.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class MissionTest {

    @Test
    @DisplayName("참여자가 미션 수행 완료를 표시한다")
    void createCheck() {
        // given
        Member creator = new Member("Sophie", "01022223333");
        Member missionMember = new Member("David", "01044445555");
        Mission mission = Mission.createMission(creator, "Speaking sentences as English");
        mission.addMember(missionMember);

        // when
        Check checkOfMissionMember = Check.of(missionMember, mission);

        // then
        List<Check> checks = checkOfMissionMember.getMission().getChecks();
        Assertions.assertThat(checks.size()).isSameAs(1);
    }
}