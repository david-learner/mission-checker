package com.missionchecker.domain;

public enum MemberRole {
    GUEST, // 로그인 하지 않은 사용자
    APPLICANT, // 미션 신청자
    PARTICIPANT, // 미션 참여자
    ADMINISTRATOR, // 미션 관리자
    OWNER // 미션 소유자(최고 관리자)
}
