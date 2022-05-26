package com.missionchecker.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MissionTest {

    private Member creator;
    private Member administrator;

    @BeforeEach
    private void setup() {
        creator = new Member("Sophie", "sophie@sophie.com", "01022223333", "12345678");
        administrator = new Member("Esther", "esther@esther.com", "01088889999", "12345678");
    }

    @Test
    @DisplayName("참여자가 미션 수행 완료를 표시한다")
    void createCheck() {
        Member missionMember = createDefaultMemberOfMission();
        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addParticipant(missionMember);

        Check checkOfMissionMember = Check.of(missionMember, mission, LocalDate.now());

        List<Check> checks = checkOfMissionMember.getMission().getChecks();
        Assertions.assertThat(checks.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자와 미션 관리자는 전체 참여자들의 미션 수행 완료 내역을 확인할 수 있다")
    void getAllChecksByAdmin() {
        Member administrator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Member missionMember = createDefaultMemberOfMission();
        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addAdministration(administrator);
        mission.addParticipant(missionMember);
        createCheckOfToday(missionMember, mission);

        List<Check> checksGettingFromMissionCreator = mission.getAllChecksBy(creator);
        List<Check> checksGettingFromAdministrator = mission.getAllChecksBy(administrator);

        Assertions.assertThat(checksGettingFromMissionCreator.size()).isSameAs(1);
        Assertions.assertThat(checksGettingFromAdministrator.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자, 미션 관리자가 아닌 사람은 전체 참여자들의 미션 수행 완료 내역을 확인할 수 없다")
    void getAllChecksByNotAdmin() {
        Member notMissionCreator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Member missionMember = createDefaultMemberOfMission();
        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addParticipant(missionMember);
        createCheckOfToday(missionMember, mission);

        Assertions.assertThatThrownBy(() -> mission.getAllChecksBy(notMissionCreator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미션에 관리자를 추가한다")
    void addAdministrator() {
        Member anotherAdministrator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Mission mission = createMissionUntilAfterMonthFromToday(creator);
        mission.addAdministration(anotherAdministrator);

        Set<Administration> administrations = mission.getAdministrations();

        Assertions.assertThat(administrations.size()).isSameAs(2);
    }

    @Test
    @DisplayName("회원은 하나의 미션에 중복 지원할 수 없다")
    void Member_can_not_apply_same_mission() {
        Mission mission = createDefaultMission();
        Member applicant = new Member("David", "david@david.com", "01044445555", "12345678");
        mission.addApplicant(applicant);

        Assertions.assertThatThrownBy(() -> mission.addApplicant(applicant))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 미션에 중복으로 신청할 수 없습니다");
    }

    @Test
    @DisplayName("회원은 하나의 미션에 중복 참여자가 될 수 없다")
    void Member_can_not_be_duplicated_participation_of_same_mission() {
        Mission mission = createDefaultMission();
        Member participant = new Member("David", "david@david.com", "01044445555", "12345678");
        mission.addParticipant(participant);

        Assertions.assertThatThrownBy(() -> mission.addParticipant(participant))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 미션에 중복으로 참여할 수 없습니다");
    }

    @Test
    @DisplayName("하나의 미션에 동일한 관리자를 추가할 수 없다")
    void Cannot_be_added_to_same_mission() {
        Mission mission = createDefaultMission();
        Member administrator = new Member("David", "david@david.com", "01044445555", "12345678");
        mission.addAdministration(administrator);

        Assertions.assertThatThrownBy(() -> mission.addAdministration(administrator))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("이미 존재하는 관리자입니다");
    }

    @Test
    @DisplayName("관리자는 미션 참여 신청을 수락할 수 있다")
    void Administrator_can_accept_applying_request_for_mission() {
        Member applicant = new Member("David", "david@david.com", "01012345678", "12345678");
        Mission mission = createDefaultMission();
        mission.addAdministration(administrator);
        mission.addApplicant(applicant);

        mission.acceptApplyingRequestBy(administrator, applicant);

        // todo 게터를 다 열고 써도 되는가? 테스트를 위한 trade-off로 보아도 되는가?
        Assertions.assertThat(mission.getApplications().size()).isZero();
        Assertions.assertThat(mission.getParticipations().size()).isEqualTo(1);
    }

    /**
     * Dummy Mission 을 생성한다 미션 시작일: 오늘, 미션 종료일: 오늘로부터 30일 후 미션 수행 표시 가능 시간: 9:30~17:30
     *
     * @param creator
     * @return
     */
    private Mission createMissionUntilAfterMonthFromToday(Member creator) {
        int THIRTY_DAYS = 30;
        String missionName = "Speaking sentences as English";
        LocalDate missionStartDate = LocalDate.now();
        LocalDate missionEndDate = LocalDate.now().plus(Period.ofDays(THIRTY_DAYS));
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

    private Mission createDefaultMission() {
        int THIRTY_DAYS = 30;
        String missionName = "Speaking sentences as English";
        LocalDate missionStartDate = LocalDate.now();
        LocalDate missionEndDate = LocalDate.now().plus(Period.ofDays(THIRTY_DAYS));
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

    private Check createCheckOfToday(Member member, Mission mission) {
        return Check.of(member, mission, LocalDate.now());
    }

    private Member createDefaultMemberOfMission() {
        return new Member("David", "david@david.com", "01044445555", "12345678");
    }
}