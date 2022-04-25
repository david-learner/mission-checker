package com.missionchecker.web;

import com.missionchecker.domain.Member;
import com.missionchecker.dto.MissionCreationRequest;
import com.missionchecker.repository.MissionRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MissionController {

    private final MissionRepository missionRepository;

    @GetMapping("/missions/registration-form")
    public String missionRegistrationForm(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "mission/registration-form";
    }

    @GetMapping("/missions")
    public String missions(Member loginMember) {
        return "mission/home";
    }

    @PostMapping("/missions")
    public String createMission(MissionCreationRequest creationRequest, Member loginMember) {
        missionRepository.save(creationRequest.toMission(loginMember));
        return "redirect:/missions/completion";
    }

    @GetMapping("/missions/completion")
    public String completeOpeningMission(Member loginMember) {
        log.debug(loginMember.getName());
        return "mission/completion";
    }
}