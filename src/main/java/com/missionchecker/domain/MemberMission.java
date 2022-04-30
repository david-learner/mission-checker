package com.missionchecker.domain;

import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public class MemberMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "MISSION_ID")
    private Mission mission;

    protected MemberMission() {
    }

    public MemberMission(Member member, Mission mission) {
        Objects.requireNonNull(member, "MemberMission 생성시 반드시 Member가 존재해야 합니다.");
        Objects.requireNonNull(mission, "MemberMission 생성시 반드시 Mission이 존재해야 합니다.");
        this.member = member;
        this.mission = mission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberMission that = (MemberMission) o;
        return Objects.equals(member, that.member) && Objects.equals(mission, that.mission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member.getName(), member.getEmail(), member.getPhone(), mission.getName());
    }
}
