package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "TB_MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "TB_MEMBER_MISSION",
            joinColumns = @JoinColumn(name = "TB_MEMBER_ID"),
            inverseJoinColumns = @JoinColumn(name = "TB_MISSION_ID")
    )
    private List<Mission> missions;
    private String phoneNumber;

    public Member() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(phoneNumber, member.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }
}
