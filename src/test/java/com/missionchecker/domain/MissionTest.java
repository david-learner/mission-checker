package com.missionchecker.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class MissionTest {

    @Test
    @DisplayName("참여자가 미션 수행 완료를 표시한다")
    void createCheck() {
        // given
        Member creator = new Member("Sophie", "sophie@sophie.com", "01022223333");
        Member missionMember = new Member("David", "david@david.com", "01044445555");
        Mission mission = Mission.createMission(creator, "Speaking sentences as English");
        mission.addMember(missionMember);

        // when
        Check checkOfMissionMember = Check.of(missionMember, mission);

        // then
        List<Check> checks = checkOfMissionMember.getMission().getChecks();
        Assertions.assertThat(checks.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자와 미션 관리자는 전체 참여자들의 미션 수행 완료 내역을 확인할 수 있다")
    void getAllChecksByAdmin() {
        // given
        Member missionCreator = new Member("Sophie", "sophie@sophie.com", "01022223333");
        Member administrator = new Member("Julie", "julie@julie.com", "01066667777");
        Member missionMember = new Member("David", "david@david.com", "01044445555");

        Mission mission = Mission.createMission(missionCreator, "Speaking sentences as English");
        mission.addAdministrator(administrator);
        mission.addMember(missionMember);

        Check.of(missionMember, mission);

        // when
        List<Check> checksGettingFromMissionCreator = mission.getAllChecksBy(missionCreator);
        List<Check> checksGettingFromAdministrator = mission.getAllChecksBy(administrator);

        // then
        Assertions.assertThat(checksGettingFromMissionCreator.size()).isSameAs(1);
        Assertions.assertThat(checksGettingFromAdministrator.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자, 미션 관리자가 아닌 사람은 전체 참여자들의 미션 수행 완료 내역을 확인할 수 없다")
    void getAllChecksByNotAdmin() {
        // given
        Member missionCreator = new Member("Sophie", "sophie@sophie.com", "01022223333");
        Member notMissionCreator = new Member("Julie", "julie@julie.com", "01066667777");
        Member missionMember = new Member("David", "david@david.com", "01044445555");

        Mission mission = Mission.createMission(missionCreator, "Speaking sentences as English");
        mission.addMember(missionMember);

        Check.of(missionMember, mission);

        // when
        // then
        Assertions.assertThatThrownBy(() -> mission.getAllChecksBy(notMissionCreator)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미션에 관리자를 추가한다")
    void addAdministrator() {
        // given
        Member missionCreator = new Member("Sophie", "sophie@sophie.com", "01022223333");
        Member anotherAdministrator = new Member("Julie", "julie@julie.com", "01066667777");


        Mission mission = Mission.createMission(missionCreator, "Speaking sentences as English");
        mission.addAdministrator(anotherAdministrator);

        // when
        Set<Member> administrators = mission.getAdministrators();

        // then
        Assertions.assertThat(administrators.size()).isSameAs(2);
    }
}