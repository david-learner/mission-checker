package com.missionchecker.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MissionTest {

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
    @DisplayName("미션 개설자와 미션 관리자는 전체 참여자들의 미션 수행 완료 내역을 확인할 수 있다")
    void Mission_creator_and_administrator_see_mission_complete_history_of_all_participants() {
        Mission mission = MissionFactory.createDefaultMission(creator);
        mission.addAdministration(administrator);
        mission.addParticipant(participant);
        Check.of(participant, mission, LocalDate.now());

        List<Check> checksGettingFromMissionCreator = mission.getAllChecksBy(creator);
        List<Check> checksGettingFromAdministrator = mission.getAllChecksBy(administrator);

        Assertions.assertThat(checksGettingFromMissionCreator.size()).isSameAs(1);
        Assertions.assertThat(checksGettingFromAdministrator.size()).isSameAs(1);
    }

    @Test
    @DisplayName("미션 개설자, 미션 관리자가 아닌 사람은 전체 참여자들의 미션 수행 완료 내역을 확인할 수 없다")
    void Member_excluded_mission_creator_and_administrator_cannot_see_mission_complete_history_of_all_participants() {
        Member notMissionCreator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Mission mission = MissionFactory.createDefaultMission(creator);
        mission.addParticipant(participant);
        Check.of(participant, mission, LocalDate.now());

        Assertions.assertThatThrownBy(() -> mission.getAllChecksBy(notMissionCreator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미션에 관리자를 추가한다")
    void Add_administrator_to_mission() {
        Member anotherAdministrator = new Member("Julie", "julie@julie.com", "01066667777", "12345678");
        Mission mission = MissionFactory.createDefaultMission(creator);
        mission.addAdministration(anotherAdministrator);

        Set<Administration> administrations = mission.getAdministrations();

        Assertions.assertThat(administrations.size()).isSameAs(2);
    }

    @Test
    @DisplayName("회원은 하나의 미션에 중복 지원할 수 없다")
    void Member_cannot_apply_same_mission() {
        Mission mission = MissionFactory.createDefaultMission(creator);
        mission.addApplicant(applicant);

        Assertions.assertThatThrownBy(() -> mission.addApplicant(applicant))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 미션에 중복으로 신청할 수 없습니다");
    }

    @Test
    @DisplayName("회원은 하나의 미션에 중복 참여자가 될 수 없다")
    void Member_cannot_be_participant_of_same_mission() {
        Mission mission = MissionFactory.createDefaultMission(creator);
        mission.addParticipant(participant);

        Assertions.assertThatThrownBy(() -> mission.addParticipant(participant))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 미션에 중복으로 참여할 수 없습니다");
    }

    @Test
    @DisplayName("관리자는 동일한 미션에 관리자로 추가될 수 없다")
    void Administrator_cannot_be_added_as_administrator_in_same_mission() {
        Mission mission = MissionFactory.createDefaultMission(creator);
        mission.addAdministration(administrator);

        Assertions.assertThatThrownBy(() -> mission.addAdministration(administrator))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("이미 존재하는 관리자입니다");
    }

    @Test
    @DisplayName("관리자는 미션 참여 신청을 수락할 수 있다")
    void Administrator_can_accept_applying_request_for_mission() {
        Mission mission = MissionFactory.createDefaultMission(creator);
        mission.addAdministration(administrator);
        mission.addApplicant(applicant);

        mission.acceptApplyingRequestBy(administrator, applicant);

        // getter 를 다 열고 쓰는 게 테스트를 위한 trade-off로 보아도 되는가?
        Assertions.assertThat(mission.getApplications().size()).isZero();
        Assertions.assertThat(mission.getParticipations().size()).isEqualTo(1);
    }
}