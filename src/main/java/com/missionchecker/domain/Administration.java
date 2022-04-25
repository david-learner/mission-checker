package com.missionchecker.domain;

import javax.persistence.Entity;

/**
 * 미션 관리자 정보
 */

@Entity
public class Administration extends MemberMission {

    protected Administration() {
    }

    public Administration(Member admin, Mission mission) {
        super(admin, mission);
    }
}
