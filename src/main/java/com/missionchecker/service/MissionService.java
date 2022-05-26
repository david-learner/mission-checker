package com.missionchecker.service;

import com.missionchecker.domain.Check;
import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import com.missionchecker.dto.MissionCreationRequest;
import com.missionchecker.dto.MissionDetailResponse;
import com.missionchecker.repository.MemberRepository;
import com.missionchecker.repository.MissionRepository;
import com.missionchecker.repository.ParticipationRepository;
import com.missionchecker.support.SessionMember;
import com.missionchecker.web.CheckRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    public static final String NO_SUCH_MISSION_MESSAGE = "미션이 존재하지 않습니다.";
    public static final String NO_SUCH_MEMBER_MESSAGE = "회원이 존재하지 않습니다.";
    public static final String NO_SUCH_PARTICIPATION_MESSAGE = "참여정보가 존재하지 않습니다.";
    private final CheckRepository checkRepository;
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;
    private final ParticipationRepository participationRepository;

    public List<Mission> findByAllMissionsCreatedBy(SessionMember sessionMember) {
        Member creator = findMemberById(sessionMember.getId());
        return missionRepository.findAllByCreatorAndIsDeleted(creator, Boolean.FALSE);
    }


    public List<Mission> findByAllParticipatingMissions(SessionMember sessionMember) {
        Member member = findMemberById(sessionMember.getId());
        return member.getParticipations().stream().map(participation -> participation.getMission())
                .collect(Collectors.toUnmodifiableList());
    }

    public MissionDetailResponse findMissionDetail(Long missionId, Long memberId) {
        Mission mission = findMissionById(missionId);
        Member member = findMemberById(memberId);
        List<Check> checks = findChecksByMissionAndMemberId(missionId, memberId);
        return new MissionDetailResponse(mission, mission.getMemberRoleOfMission(member), checks);
    }

    @Transactional
    public void createMission(MissionCreationRequest missionCreationRequest, SessionMember sessionMember) {
        Member creator = findMemberById(sessionMember.getId());
        missionRepository.save(missionCreationRequest.toMission(creator));
    }

    @Transactional
    public void applyMission(SessionMember sessionMember, Long missionId) {
        Member applicant = findMemberById(sessionMember.getId());
        Mission mission = findMissionById(missionId);
        mission.addApplicant(applicant);
    }

    @Transactional
    public void acceptApplyingRequest(Long missionId, Long applicantId, SessionMember sessionMember) {
        Member applicant = findMemberById(applicantId);
        Member loginMember = findMemberById(sessionMember.getId());
        Mission mission = findMissionById(missionId);
        mission.acceptApplyingRequestBy(loginMember, applicant);
    }

    public void validateParticipantOfMission(Long memberId, Long missionId) {
        participationRepository.findParticipationByMemberIdAndMissionId(memberId, missionId).orElseThrow(() -> {
            throw new NoSuchElementException(NO_SUCH_PARTICIPATION_MESSAGE);
        });
    }

    @Transactional
    public void createCheck(Long missionId, Long memberId, LocalDate missionExecutionDate) {
        Mission mission = findMissionById(missionId);
        Member member = findMemberById(memberId);
        Check check = Check.of(member, mission, missionExecutionDate);
        checkRepository.save(check);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementException(NO_SUCH_MEMBER_MESSAGE);
        });
    }

    private Mission findMissionById(Long id) {
        return missionRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementException(NO_SUCH_MISSION_MESSAGE);
        });
    }

    private List<Check> findChecksByMissionAndMemberId(Long missionId, Long memberId) {
        return checkRepository.findAllByMissionIdAndCheckerId(missionId, memberId);
    }
}
