package com.missionchecker.web;

import com.missionchecker.service.MissionService;
import com.missionchecker.support.SessionMember;
import com.missionchecker.util.Constant;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MissionService missionService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        // Home의 경우 로그인, 비로그인 사용자 서로 다른 화면을 보여줘야 한다.
        // SessionMember loginMember로 로그인 처리를 하게 되면 미로그인 사용자가 접근시 오류를 발생시킨다.
        // todo 로그인, 비로그인 사용자 모두가 공존하는 페이지를 어떻게 제공하면 좋을지 생각하자
        SessionMember loginMember = (SessionMember) session.getAttribute(Constant.LOGIN_MEMBER);
        if (!Objects.isNull(loginMember)) {
            model.addAttribute("participatingMissions", missionService.findByAllParticipatingMissions(loginMember));
        }
        return "index";
    }
}
