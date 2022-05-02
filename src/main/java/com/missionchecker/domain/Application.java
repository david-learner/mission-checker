package com.missionchecker.domain;

import javax.persistence.Entity;

/**
 * 미션 참여 신청자 정보
 */
@Entity
public class Application extends MemberMission {

    protected Application() {
    }

    public Application(Member member, Mission mission) {
        super(member, mission);
    }
}
