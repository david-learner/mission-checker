package com.missionchecker.web;

import com.missionchecker.dto.SigningUpRequest;
import com.missionchecker.service.SigningUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SigningUpController {

    private final SigningUpService signingUpService;

    @GetMapping("/sign-up")
    public String signingUpForm() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signingUp(SigningUpRequest signingUpRequest) {
        signingUpService.signUp(signingUpRequest);
        return "redirect:/sign-up/completion";
    }

    @GetMapping("/sign-up/completion")
    public String signingUpComplete() {
        return "sign-up-completion";
    }
}
