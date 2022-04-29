package com.missionchecker.web;

import com.missionchecker.dto.LoginRequest;
import com.missionchecker.service.LoginService;
import com.missionchecker.support.SessionMember;
import com.missionchecker.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

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
        session.setAttribute(Constant.LOGIN_MEMBER, SessionMember.of(loginService.login(loginRequest)));
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
