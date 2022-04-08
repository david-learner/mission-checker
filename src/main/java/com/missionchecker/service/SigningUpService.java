package com.missionchecker.service;

import com.missionchecker.dto.SigningUpRequest;
import com.missionchecker.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SigningUpService {

    private final MemberRepository memberRepository;

    /**
     * 중복된 이메일인지 확인 후 회원으로 가입시킨다.
     * @param signingUpRequest
     */
    public void signUp(SigningUpRequest signingUpRequest) {
        memberRepository.findTopByEmail(signingUpRequest.getEmail()).ifPresent(member -> {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        });
        memberRepository.save(signingUpRequest.toMember());
    }
}
