package com.missionchecker.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MissionTest {

    @Test
    @DisplayName("참여자가 미션 수행 완료를 표시한다")
    void createCheck() {
        // given
        Member creator = new Member("Sophie", "sophie@sophie.com", "01022223333", "12345678");
        Member missionMember = new Member("David", "david@david.com", "01044445555", "12345678");
        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addParticipant(missionMember);

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
        Member creator = new Member("Sophie", "sophie@sophie.com", "01022223333", "12345678");
        Member administrator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Member missionMember = new Member("David", "david@david.com", "01044445555", "12345678");

        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addAdministration(new Administration(administrator, mission));
        mission.addParticipant(missionMember);

        Check.of(missionMember, mission);

        // when
        List<Check> checksGettingFromMissionCreator = mission.getAllChecksBy(creator);
        List<Check> checksGettingFromAdministrator = mission.getAllChecksBy(administrator);

        // then
        Assertions.assertThat(checksGettingFromMissionCreator.size()).isSameAs(1);
        Assertions.assertThat(checksGettingFromAdministrator.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자, 미션 관리자가 아닌 사람은 전체 참여자들의 미션 수행 완료 내역을 확인할 수 없다")
    void getAllChecksByNotAdmin() {
        // given
        Member creator = new Member("Sophie", "sophie@sophie.com", "01022223333", "12345678");
        Member notMissionCreator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Member missionMember = new Member("David", "david@david.com", "01044445555", "12345678");

        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addParticipant(missionMember);

        Check.of(missionMember, mission);

        // when
        // then
        Assertions.assertThatThrownBy(() -> mission.getAllChecksBy(notMissionCreator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미션에 관리자를 추가한다")
    void addAdministrator() {
        // given
        Member creator = new Member("Sophie", "sophie@sophie.com", "01022223333", "12345678");
        Member anotherAdministrator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");

        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addAdministration(new Administration(anotherAdministrator, mission));

        // when
        Set<Administration> administrations = mission.getAdministrations();

        // then
        Assertions.assertThat(administrations.size()).isSameAs(2);
    }

    private Mission createMissionUntilAfterMonthFromToday(Member creator) {
        int THIRTY_DAYS = 30;
        String missionName = "Speaking sentences as English";
        LocalDate missionStartDate = LocalDate.now();
        LocalDate missionEndDate = LocalDate.now().plus(Duration.ofDays(THIRTY_DAYS));
        LocalTime missionCheckStartTime = LocalTime.of(9, 30);
        LocalTime missionCheckEndTime = LocalTime.of(17, 30);
        MissionConfiguration configuration = new MissionConfiguration(
                missionStartDate,
                missionEndDate,
                missionCheckStartTime,
                missionCheckEndTime
        );
        return Mission.of(creator, missionName, configuration);
    }
}