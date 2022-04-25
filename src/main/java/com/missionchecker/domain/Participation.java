package com.missionchecker.domain;

import javax.persistence.Entity;

/**
 * 미션 참여자 정보
 */
@Entity
public class Participation extends MemberMission {

    protected Participation() {
    }

    public Participation(Member member, Mission mission) {
        super(member, mission);
    }
}
