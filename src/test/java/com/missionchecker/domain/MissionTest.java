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

    // 왜 생성 메서드를 사용하지 않고 beforeEach를 통해 픽스처를 초기화하는가?
    // Member의 경우 초기화시 서로 다른 테스트에 영향을 줄만한 내부 상태를 갖지 않기 때문이다.
    private Member creator;
    private Member administrator;
    private Member participant;
    private Member applicant;

    @BeforeEach
    void setup() {
        creator = new Member("윤쏘피", "sophie@sophie.com", "01011111111", "12345678");
        administrator = new Member("화줌마", "flora@esther.com", "01022222222", "12345678");
        participant = new Member("데이빗", "david@david.com", "01033333333", "12345678");
        applicant = new Member("장짱구", "snow@david.com", "01044444444", "12345678");
    }

    @Test
    @DisplayName("참여자가 미션 수행 완료를 표시한다")
    void Participant_marks_for_mission_complete() {
        Mission mission = createDefaultMission();
        mission.addParticipant(participant);

        Check checkOfMissionMember = Check.of(participant, mission, LocalDate.now());

        List<Check> checks = checkOfMissionMember.getMission().getChecks();
        Assertions.assertThat(checks.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자와 미션 관리자는 전체 참여자들의 미션 수행 완료 내역을 확인할 수 있다")
    void Mission_creator_and_administrator_see_mission_complete_history_of_all_participants() {
        Mission mission = createDefaultMission();
        mission.addAdministration(administrator);
        mission.addParticipant(participant);
        createCheckOfToday(participant, mission);

        List<Check> checksGettingFromMissionCreator = mission.getAllChecksBy(creator);
        List<Check> checksGettingFromAdministrator = mission.getAllChecksBy(administrator);

        Assertions.assertThat(checksGettingFromMissionCreator.size()).isSameAs(1);
        Assertions.assertThat(checksGettingFromAdministrator.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자, 미션 관리자가 아닌 사람은 전체 참여자들의 미션 수행 완료 내역을 확인할 수 없다")
    void Member_excluded_mission_creator_and_administrator_cannot_see_mission_complete_history_of_all_participants() {
        Member notMissionCreator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Mission mission = createDefaultMission();
        mission.addParticipant(participant);
        createCheckOfToday(participant, mission);

        Assertions.assertThatThrownBy(() -> mission.getAllChecksBy(notMissionCreator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미션에 관리자를 추가한다")
    void Add_administrator_to_mission() {
        Member anotherAdministrator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Mission mission = createDefaultMission();
        mission.addAdministration(anotherAdministrator);

        Set<Administration> administrations = mission.getAdministrations();

        Assertions.assertThat(administrations.size()).isSameAs(2);
    }

    @Test
    @DisplayName("회원은 하나의 미션에 중복 지원할 수 없다")
    void Member_cannot_apply_same_mission() {
        Mission mission = createDefaultMission();
        mission.addApplicant(applicant);

        Assertions.assertThatThrownBy(() -> mission.addApplicant(applicant))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 미션에 중복으로 신청할 수 없습니다");
    }

    @Test
    @DisplayName("회원은 하나의 미션에 중복 참여자가 될 수 없다")
    void Member_cannot_be_participant_of_same_mission() {
        Mission mission = createDefaultMission();
        mission.addParticipant(participant);

        Assertions.assertThatThrownBy(() -> mission.addParticipant(participant))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 미션에 중복으로 참여할 수 없습니다");
    }

    @Test
    @DisplayName("관리자는 동일한 미션에 관리자로 추가될 수 없다")
    void Administrator_cannot_be_added_as_administrator_in_same_mission() {
        Mission mission = createDefaultMission();
        mission.addAdministration(administrator);

        Assertions.assertThatThrownBy(() -> mission.addAdministration(administrator))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("이미 존재하는 관리자입니다");
    }

    @Test
    @DisplayName("관리자는 미션 참여 신청을 수락할 수 있다")
    void Administrator_can_accept_applying_request_for_mission() {
        Mission mission = createDefaultMission();
        mission.addAdministration(administrator);
        mission.addApplicant(applicant);

        mission.acceptApplyingRequestBy(administrator, applicant);

        // getter 를 다 열고 쓰는 게 테스트를 위한 trade-off로 보아도 되는가?
        Assertions.assertThat(mission.getApplications().size()).isZero();
        Assertions.assertThat(mission.getParticipations().size()).isEqualTo(1);
    }

    /**
     * 미션을 생성한다 시작일: 오늘, 미션 종료일: 오늘로부터 30일 후 미션 수행 표시 가능 시간: 9:30~17:30
     */
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
                missionCheckEndTime,
                true
        );

        return Mission.of(creator, missionName, configuration);
    }

    /**
     * 오늘 날짜를 가지는 미션 수행 완료 표시를 생성한다.
     *
     * @param member
     * @param mission
     * @return
     */
    private Check createCheckOfToday(Member member, Mission mission) {
        return Check.of(member, mission, LocalDate.now());
    }
}