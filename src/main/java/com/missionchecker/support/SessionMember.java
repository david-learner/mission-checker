package com.missionchecker.support;

import com.missionchecker.domain.Member;
import lombok.Getter;

/**
 * 세션 내 저장될 회원 정보
 */
@Getter
public class SessionMember {

    private Long id;
    private String name;
    private String email;

    private SessionMember(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static SessionMember of(Member member) {
        return new SessionMember(member.getId(), member.getName(), member.getEmail());
    }
}
