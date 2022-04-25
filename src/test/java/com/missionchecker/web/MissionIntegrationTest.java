package com.missionchecker.web;

import com.missionchecker.domain.Member;
import com.missionchecker.dto.MissionCreationRequest;
import com.missionchecker.repository.MemberRepository;
import com.missionchecker.repository.MissionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MissionIntegrationTest {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("한 명의 사용자는 2개 이상의 미션을 개설할 수 있다")
    void createMultipleMissionFromOneCreator(){
        // given
        Member member = new Member("david", "david@david.com", "01011112222", "1234");
        Member savedMember = memberRepository.save(member);

        MissionCreationRequest missionCreationRequest1 = new MissionCreationRequest();
        missionCreationRequest1.setName("쏘리클1");
        missionRepository.save(missionCreationRequest1.toMission(savedMember));

        MissionCreationRequest missionCreationRequest2 = new MissionCreationRequest();
        missionCreationRequest2.setName("쏘리클2");

        // when
        missionRepository.save(missionCreationRequest2.toMission(savedMember));

        // then
        Assertions.assertThat(savedMember.getNumberOfMissionsAsAdministrator()).isGreaterThan(1);
    }
}
