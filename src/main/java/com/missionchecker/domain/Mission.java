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

    private static final String DUPLICATION_APPLYING_MESSAGE = "동일한 미션에 중복으로 신청할 수 없습니다";
    private static final String DUPLICATION_PARTICIPATING_MESSAGE = "동일한 미션에 중복으로 참여할 수 없습니다";
    private static final String NOT_ADMINISTRATOR_MESSAGE = "해당 미션 관리자가 아닙니다";
    private static final String DUPLICATED_ADMINISTRATOR_MESSAGE = "이미 존재하는 관리자입니다";

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
        Set<Participation> participations = new HashSet<>();
        Set<Application> applications = new HashSet<>();
        List<Check> checks = new ArrayList<>();
        Set<Administration> administrations = new HashSet<>();

        Mission mission = new Mission(
                creator,
                missionName,
                applications,
                participations,
                checks,
                administrations,
                configuration);
        mission.addAdministration(creator);
        return mission;
    }

    public void addApplicant(Member applicant) {
        Objects.requireNonNull(applicant);
        if (!isExisted(applicant)) {
            applications.add(new Application(applicant, this));
            return;
        }
        throw new IllegalArgumentException(DUPLICATION_APPLYING_MESSAGE);
    }

    public void addParticipant(Member participant) {
        Objects.requireNonNull(participant);
        if (!isExisted(participant)) {
            Participation participation = new Participation(participant, this);
            this.participations.add(participation);
            // 연관관계 편의 메서드
            participant.addParticipation(participation);
            return;
        }
        throw new IllegalArgumentException(DUPLICATION_PARTICIPATING_MESSAGE);
    }

    private boolean isExisted(Member member) {
        return isApplicant(member) || isParticipant(member);
    }

    public void addAdministration(Member administrator) {
        if (!isAdministrator(administrator)) {
            Administration administration = new Administration(administrator, this);
            administrations.add(administration);
            // 연관관계 편의 메서드
            administration.getMember().addAdministration(administration);
            return;
        }
        throw new IllegalArgumentException(DUPLICATED_ADMINISTRATOR_MESSAGE);
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

    public boolean canRegisterWithPastDate() {
        return configuration.canRegisterWithPastDate();
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
