package com.missionchecker.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class MissionTest {

    @Test
    @DisplayName("미션 개설자는 전체 참여자들의 미션 수행 완료 내역을 확인할 수 있다")
    void getAllChecksByAdmin() {
        // given
        Member missionCreator = new Member("Sophie", "01022223333");
        Member missionMember = new Member("David", "01044445555");

        Mission mission = Mission.createMission(missionCreator, "Speaking sentences as English");
        mission.addMember(missionMember);

        Check.of(missionMember, mission);

        // when
        List<Check> checks = mission.getAllChecksBy(missionCreator);

        // then
        Assertions.assertEquals(1, checks.size());
    }

    @Test
    @DisplayName("미션 개설자, 미션 관리자가 아닌 사람은 전체 참여자들의 미션 수행 완료 내역을 확인할 수 없다")
    void getAllChecksByNotAdmin() {
        // given
        Member missionCreator = new Member("Sophie", "01022223333");
        Member notMissionCreator = new Member("Julie", "01066667777");
        Member missionMember = new Member("David", "01044445555");

        Mission mission = Mission.createMission(missionCreator, "Speaking sentences as English");
        mission.addMember(missionMember);

        Check.of(missionMember, mission);

        // when
        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> mission.getAllChecksBy(notMissionCreator));
    }
}