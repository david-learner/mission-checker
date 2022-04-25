package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "MISSION")
@Getter
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // 미션 참여자 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private Set<Participation> participations;
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<Check> checks;
    // 미션 관리자 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private Set<Administration> administrations;
    private LocalDate openDate;
    private LocalDate closeDate;

    public Mission() {
    }

    private Mission(String name, Set<Participation> participations, List<Check> checks, Set<Administration> administrations, LocalDate openDate, LocalDate closeDate) {
        this.name = name;
        this.participations = participations;
        this.checks = checks;
        this.administrations = administrations;
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    /**
     * 미션을 생성한다. 미션을 생성한 사람은 자동으로 미션의 관리자로 추가된다.
     * @param creator
     * @param missionName
     * @return
     */
    public static Mission of(Member creator, String missionName) {
        // 초기화
        Set<Participation> participations = new HashSet<>();
        List<Check> checks = new ArrayList<>();
        Set<Administration> administrations = new HashSet<>();
        LocalDate now = LocalDate.now();

        // 생성
        Mission mission = new Mission(missionName, participations, checks, administrations, now, now);
        mission.addAdministration(new Administration(creator, mission));
        return mission;
    }

    public void addParticipant(Member participant) {
        // Participation은 mission, member 정보가 null이어선 안 된다.
        // POJO로 not null validation을 어떻게 처리하면 좋을까?
        Participation participation = new Participation(participant, this);
        this.participations.add(participation);
        // todo 미션에 참여자가 추가되면 참여자에게도 미션이 추가되어야 한다. 연관관계 편의 메서드?
        participant.addParticipation(participation);
    }

    public List<Check> getAllChecksBy(Member administrator) {
        if (isAdministrator(administrator)) {
            return checks;
        }
        throw new IllegalArgumentException("해당 미션 관리자가 아닙니다");
    }

    private boolean isAdministrator(Member administrator) {
        for (Administration administration : administrations) {
            if (administration.isAdministrator(administrator)) {
                return true;
            }
        }
        return false;
    }

    // 미션 생성할 때 생성자의 관리자 정보 목록에 관리자 정보 추가하기
    public void addAdministration(Administration administration) {
        // Mission의 Administration
        administrations.add(administration);
        // Member의 Administration
        administration.getMember().addAdministration(administration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return Objects.equals(name, mission.name) && Objects.equals(openDate, mission.openDate) && Objects.equals(closeDate, mission.closeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, openDate, closeDate);
    }
}
