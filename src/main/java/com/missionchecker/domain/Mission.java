package com.missionchecker.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
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
public class Mission extends BaseEntity {

    public static final String DUPLICATION_APPLYING_MESSAGE = "동일한 미션에 중복으로 신청할 수 없습니다";
    public static final String NOT_ADMINISTRATOR_MESSAGE = "해당 미션 관리자가 아닙니다";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne
    private Member creator;
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<Check> checks;
    // 미션 신청 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;
    // 미션 참여자 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private Set<Participation> participations;
    // 미션 관리자 정보
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private Set<Administration> administrations;
    @Embedded
    private MissionConfiguration configuration;

    protected Mission() {
        super(LocalDateTime.now(), false);
    }

    private Mission(Member creator, String name, Set<Application> applications, Set<Participation> participations,
                    List<Check> checks, Set<Administration> administrations, MissionConfiguration configuration) {
        super(LocalDateTime.now(), false);
        this.creator = creator;
        this.name = name;
        this.applications = applications;
        this.participations = participations;
        this.checks = checks;
        this.administrations = administrations;
        this.configuration = configuration;
    }

    /**
     * 미션을 생성한다. 미션을 생성한 사람은 자동으로 미션의 관리자로 추가된다.
     *
     * @param creator
     * @param missionName
     * @return
     */
    public static Mission of(Member creator, String missionName, MissionConfiguration configuration) {
        // 초기화
        Set<Participation> participations = new HashSet<>();
        Set<Application> applications = new HashSet<>();
        List<Check> checks = new ArrayList<>();
        Set<Administration> administrations = new HashSet<>();

        // 생성
        Mission mission = new Mission(
                creator,
                missionName,
                applications,
                participations,
                checks,
                administrations,
                configuration);
        mission.addAdministration(new Administration(creator, mission));
        return mission;
    }

    public void addApplicant(Member member) {
        // todo set 중복 삽입시 발생하는 예외 어떻게 처리해줄건지
        if (!isApplicant(member) && !isParticipant(member)) {
            applications.add(new Application(member, this));
            return;
        }
        throw new IllegalArgumentException(DUPLICATION_APPLYING_MESSAGE);
    }

    public void addParticipant(Member participant) {
        // Participation은 mission, member 정보가 null이어선 안 된다.
        // POJO로 not null validation을 어떻게 처리하면 좋을까?
        Participation participation = new Participation(participant, this);
        this.participations.add(participation);
        // todo 미션에 참여자가 추가되면 참여자에게도 미션이 추가되어야 한다. 연관관계 편의 메서드?
        participant.addParticipation(participation);
    }

    // 양방향 연관관계 편의메서드
    // 미션 생성할 때 미션 생성자(creator)의 관리자 정보 목록에 관리자(creator) 정보 추가하기
    public void addAdministration(Administration administration) {
        administrations.add(administration);
        administration.getMember().addAdministration(administration);
    }

    public void acceptApplyingRequestBy(Member administrator, Member applicant) {
        validateValidAdministrator(administrator);
        try {
            applications.remove(new Application(applicant, this));
            participations.add(new Participation(applicant, this));
        } catch (Exception e) {
            // todo rollback
        }
    }

    /**
     * 해당 미션에서 어떤 역할을 맡고 있는지 확인한다
     *
     * @param member
     * @return 미션에서 맡은 역할
     */
    public MemberRole getMemberRoleOfMission(Member member) {
        if (creator.equals(member) || isAdministrator(member)) {
            return MemberRole.ADMINISTRATOR;
        }
        if (isParticipant(member)) {
            return MemberRole.PARTICIPANT;
        }
        if (isApplicant(member)) {
            return MemberRole.APPLICANT;
        }
        return MemberRole.GUEST;
    }

    public List<Check> getAllChecksBy(Member member) {
        validateValidAdministrator(member);
        return checks;
    }

    public boolean isAdministrator(Member member) {
        return administrations.contains(new Administration(member, this));
    }

    private boolean isParticipant(Member member) {
        return participations.contains(new Participation(member, this));
    }

    private boolean isApplicant(Member member) {
        return applications.contains(new Application(member, this));
    }

    public void validateValidAdministrator(Member administrator) {
        if (!isAdministrator(administrator)) {
            throw new IllegalArgumentException(NOT_ADMINISTRATOR_MESSAGE);
        }
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
        return Objects.hash(creator.getName(), creator.getEmail(), creator.getPhone(), name);
    }
}
