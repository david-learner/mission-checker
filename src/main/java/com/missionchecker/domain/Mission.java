package com.missionchecker.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "MISSION")
@Getter
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne
    private Member creator;
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<Check> checks;
    // 미션 신청 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private Set<Application> applications;
    // 미션 참여자 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private Set<Participation> participations;
    // 미션 관리자 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private Set<Administration> administrations;
    // todo boolean to bit converter 적용
    @Column(columnDefinition = "boolean default false")
    private Boolean isClosed = Boolean.FALSE;
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = Boolean.FALSE;
    private LocalDate openDate;
    private LocalDate closeDate;

    public Mission() {
    }

    private Mission(Member creator, String name, Set<Application> applications, Set<Participation> participations,
                    List<Check> checks, Set<Administration> administrations, LocalDate openDate, LocalDate closeDate) {
        this.creator = creator;
        this.name = name;
        this.applications = applications;
        this.participations = participations;
        this.checks = checks;
        this.administrations = administrations;
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    /**
     * 미션을 생성한다. 미션을 생성한 사람은 자동으로 미션의 관리자로 추가된다.
     *
     * @param creator
     * @param missionName
     * @return
     */
    public static Mission of(Member creator, String missionName) {
        // 초기화
        Set<Participation> participations = new HashSet<>();
        Set<Application> applications = new HashSet<>();
        List<Check> checks = new ArrayList<>();
        Set<Administration> administrations = new HashSet<>();
        LocalDate now = LocalDate.now();

        // 생성
        Mission mission = new Mission(creator, missionName, applications, participations, checks, administrations, now,
                now);
        mission.addAdministration(new Administration(creator, mission));
        return mission;
    }

    public void addApplicant(Member applicant) {
        // todo set 중복 삽입시 발생하는 예외 어떻게 처리해줄건지
        Application newApplication = new Application(applicant, this);
        if(!applications.contains(newApplication)) {
            applications.add(newApplication);
            return;
        }
        throw new IllegalArgumentException("동일한 미션에 중복신청할 수 없습니다");
    }
    
    public boolean isParticipating(Member participant) {
        return participations.contains(new Participation(participant, this));
    }

    public void addParticipant(Member participant) {
        // Participation은 mission, member 정보가 null이어선 안 된다.
        // POJO로 not null validation을 어떻게 처리하면 좋을까?
        Participation participation = new Participation(participant, this);
        this.participations.add(participation);
        // todo 미션에 참여자가 추가되면 참여자에게도 미션이 추가되어야 한다. 연관관계 편의 메서드?
        participant.addParticipation(participation);
    }

    // 미션 생성할 때 생성자의 관리자 정보 목록에 관리자 정보 추가하기
    public void addAdministration(Administration administration) {
        // Mission의 Administration
        administrations.add(administration);
        // Member의 Administration
        administration.getMember().addAdministration(administration);
    }

    public void acceptApplyingRequest(Member applicant) {
        try {
            applications.remove(applicant);
            participations.add(new Participation(applicant, this));
        } catch (Exception e) {
            // todo rollback
        }
    }

    public List<Check> getAllChecksBy(Member administrator) {
        if (isAdministrator(administrator)) {
            return checks;
        }
        throw new IllegalArgumentException("해당 미션 관리자가 아닙니다");
    }

    public boolean isAdministrator(Member administrator) {
        for (Administration administration : administrations) {
            if (administration.isAdministrator(administrator)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mission mission = (Mission) o;
        return Objects.equals(creator, mission.creator) && Objects.equals(name, mission.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, name);
    }
}
