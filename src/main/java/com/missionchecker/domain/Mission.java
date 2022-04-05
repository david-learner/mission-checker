package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MISSION")
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
    @JoinTable(name = "MISSION_ADMINISTRATOR")
    private Set<Member> administrators;
    private LocalDate startDate;
    private LocalDate endDate;

    public Mission() {
    }

    private Mission(String name, Set<Member> members, List<Check> checks, Set<Member> administrators, LocalDate startDate, LocalDate endDate) {

        this.name = name;
        this.members = members;
        this.checks = checks;
        this.administrators = administrators;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    static Mission createMission(Member creator, String missionName) {
        Set initialSet = new HashSet();
        initialSet.add(creator);

        return new Mission(missionName, initialSet, new ArrayList<>(), initialSet, LocalDate.now(), LocalDate.now());
    }

    public void addMember(Member member) {

        this.members.add(member);
    }

    public boolean hasMember(Member member) {

        return members.contains(member);
    }

    public List<Check> getAllChecksBy(Member administrator) {
        if(administrators.contains(administrator)) {
            return checks;
        }
        throw new IllegalArgumentException("해당 미션 관리자가 아닙니다");
    }
}
