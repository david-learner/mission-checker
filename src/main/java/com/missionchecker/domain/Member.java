package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

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

    public Member() {
    }
}
