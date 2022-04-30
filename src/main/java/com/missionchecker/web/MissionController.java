package com.missionchecker.web;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import com.missionchecker.dto.MissionCreationRequest;
import com.missionchecker.dto.MissionDetailResponse;
import com.missionchecker.service.MissionService;
import com.missionchecker.support.SessionMember;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/missions")
    public String missions(SessionMember loginMember, Model model) {
        List<Mission> missionsCreatedByMember = missionService.findByAllMissionsCreatedBy(loginMember);
        List<Mission> participatingMissions = missionService.findByAllParticipatingMissions(loginMember);
        model.addAttribute("missionsCreatedByMember", missionsCreatedByMember);
        model.addAttribute("participatingMissions", participatingMissions);
        return "mission/home";
    }

    @PostMapping("/missions")
    public String createMission(MissionCreationRequest creationRequest, SessionMember loginMember) {
        missionService.createMission(creationRequest, loginMember);
        return "redirect:/missions/completion";
    }

    @GetMapping("/missions/registration-form")
    public String missionRegistrationForm(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "mission/registration-form";
    }

    @GetMapping("/missions/completion")
    public String completeOpeningMission(Member loginMember) {
        return "mission/completion";
    }

    @GetMapping("/missions/{missionId}")
    public String missionDetailForm(@PathVariable Long missionId, SessionMember loginMember, Model model) {
        // todo missionService로부터 MissionDetailResponse DTO를 만들어서 반환받는게 나을까?
        // 지금은 MissionDetail에 들어가는 내용이 적어서 불필요해보인다. 나중에 mission 상세화면에서 보여줄게 많으면
        // 자연스럽게 MissionDetailResponse를 반환해주는 메서드가 서비스 내에서 필요할 것이다.
        MissionDetailResponse missionDetail = missionService.findMissionDetail(missionId, loginMember.getId());
        model.addAttribute("missionDetail", missionDetail);
        return "mission/detail";
    }

//    @GetMapping("/missions/{missionId}/invite")
//    public String invitationForm(@PathVariable Long missionId, SessionMember loginMember, Model model) {
//        missionService.isAdministrator(missionId, loginMember);
//        model.addAttribute("mission", missionService.findOneById(missionId));
//        return "mission/invitation-form";
//    }

    /**
     * 미션 참여 신청한다
     *
     * @param missionId
     * @param loginMember
     * @param model
     * @return
     */
    @PostMapping("/missions/{missionId}/apply")
    public ResponseEntity applyMission(@PathVariable Long missionId, SessionMember loginMember, Model model) {
        missionService.applyMission(loginMember, missionId);
        return ResponseEntity.ok().build();
    }

    /**
     * 참여 신청을 수락한다
     *
     * @param missionId
     * @param loginMember
     * @param model
     * @return
     */
    @PostMapping("/missions/{missionId}/applicants/{applicantId}/accept")
    public ResponseEntity acceptApplicant(@PathVariable Long missionId, @PathVariable Long applicantId,
                                          SessionMember loginMember, Model model) {
        missionService.acceptApplyingRequest(missionId, applicantId, loginMember);
        return ResponseEntity.ok().build();
    }
}
