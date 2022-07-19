package com.missionchecker.web;

import com.missionchecker.dto.LoginRequest;
import com.missionchecker.service.LoginService;
import com.missionchecker.support.SessionMember;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpSession session) {
        session.setAttribute(SessionMember.LOGIN_MEMBER_SESSION_KEY,
                SessionMember.of(loginService.login(loginRequest)));
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
