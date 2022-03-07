package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "TB_MISSION")
@Getter
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "missions")
    private Set<Member> members;
    @OneToMany(mappedBy = "mission")
    private List<Check> checks;
    @OneToMany
    @JoinTable(name = "TB_MISSION_ADMINISTRATOR")
    private Set<Member> administrators;
    private LocalDate startDate;
    private LocalDate endDate;

    public Mission() {
    }

    public Mission(String name, Set<Member> members, List<Check> checks, Set<Member> administrators, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.members = members;
        this.checks = checks;
        this.administrators = administrators;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    static Mission createMission(Member creator, String missionName) {
        return new Mission(missionName, Set.of(creator), new ArrayList<>(), Set.of(creator), LocalDate.now(), LocalDate.now());
    }

    public void addMember(Member member) {

        this.members.add(member);
    }
}
