package com.missionchecker.service;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.MemberRole;
import com.missionchecker.domain.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final MissionService missionService;

    public boolean hasValidRoleForAccess(MemberRole requestedRole, Long missionId, Long memberId) {
        Mission mission = missionService.findMissionById(missionId);
        Member member = missionService.findMemberById(memberId);
        // 미션 소유자는 요구되는 역할에 영향을 받지 않는다
        if (mission.isOwner(member)) {
            return true;
        }
        if (requestedRole == MemberRole.ADMINISTRATOR) {
            return mission.isAdministrator(member);
        }
        return false;
    }
}
