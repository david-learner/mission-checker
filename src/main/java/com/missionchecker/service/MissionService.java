package com.missionchecker.service;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import com.missionchecker.dto.MissionCreationRequest;
import com.missionchecker.repository.MemberRepository;
import com.missionchecker.repository.MissionRepository;
import com.missionchecker.support.SessionMember;
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
    private final MissionRepository missionRepository;
    private final MemberRepository memberRepository;

    public List<Mission> findByAllMissionsCreatedBy(SessionMember sessionMember) {
        Member creator = findMemberById(sessionMember.getId());
        return missionRepository.findAllByCreatorAndIsDeleted(creator, Boolean.FALSE);
    }


    public List<Mission> findByAllParticipatingMissions(SessionMember sessionMember) {
        Member member = findMemberById(sessionMember.getId());
        return member.getParticipations().stream().map(participation -> participation.getMission())
                .collect(Collectors.toUnmodifiableList());
    }

    public Mission findOneById(Long id) {
        return missionRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementException(NO_SUCH_MISSION_MESSAGE);
        });
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

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementException(NO_SUCH_MEMBER_MESSAGE);
        });
    }

    @Transactional
    protected Mission findMissionById(Long id) {
        return missionRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementException(NO_SUCH_MISSION_MESSAGE);
        });
    }
}
