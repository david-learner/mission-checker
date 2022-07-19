package com.missionchecker.domain;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CheckTest {

    private Member owner;
    private Member administrator;
    private Member participant;
    private Member applicant;

    @BeforeEach
    void setup() {
        owner = new Member("윤쏘피", "sophie@sophie.com", "01011111111", "12345678");
        administrator = new Member("화줌마", "flora@esther.com", "01022222222", "12345678");
        participant = new Member("데이빗", "david@david.com", "01033333333", "12345678");
        applicant = new Member("장짱구", "snow@david.com", "01044444444", "12345678");
    }

    @Test
    @DisplayName("참여자가 미션 수행 완료를 표시한다")
    void Participant_marks_for_mission_complete() {
        Mission mission = MissionFactory.createDefaultMission(owner);
        mission.addParticipant(participant);

        Check checkOfMissionMember = Check.of(participant, mission, LocalDate.now());

        List<Check> checks = checkOfMissionMember.getMission().getChecks();
        Assertions.assertThat(checks.size()).isSameAs(1);
    }
}
