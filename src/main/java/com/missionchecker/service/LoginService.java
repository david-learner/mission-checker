package com.missionchecker.service;

import com.missionchecker.domain.Member;
import com.missionchecker.dto.LoginRequest;
import com.missionchecker.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;

    // todo 서비스가 DTO에 의존해도 괜찮은가?
    public Member login(LoginRequest loginRequest) {
        Member foundMember = memberRepository.findTopByEmail(loginRequest.getEmail()).orElseThrow(() -> {
            throw new IllegalArgumentException("이메일이 존재하지 않습니다.");
        });

        if (!foundMember.isSamePassword(loginRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return foundMember;
    }
}
