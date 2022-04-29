package com.missionchecker.web;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import com.missionchecker.dto.MissionCreationRequest;
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
//        List<Mission> participatingMissions = missionService.findByAllParticipatingMissions(loginMember);
        model.addAttribute("missionsCreatedByMember", missionsCreatedByMember);
//        model.addAttribute("participatingMissions", participatingMissions);
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
    public String missionDetailForm(@PathVariable Long missionId, Model model) {
        model.addAttribute("mission", missionService.findOneById(missionId));
        return "mission/detail";
    }

    @PostMapping("/missions/{missionId}/apply")
    public ResponseEntity applyMission(@PathVariable Long missionId, SessionMember loginMember, Model model) {
        missionService.applyMission(loginMember, missionId);
        return ResponseEntity.ok().build();
    }
}
