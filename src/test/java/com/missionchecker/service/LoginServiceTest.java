package com.missionchecker.service;

import com.missionchecker.domain.Member;
import com.missionchecker.dto.LoginRequest;
import com.missionchecker.repository.MemberRepository;
import com.missionchecker.test.support.Constant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 할 수 없다")
    void loginWithNotExistedEmail(){
        // given
        LoginRequest loginRequest = new LoginRequest(Constant.DAVID_EMAIL, Constant.DEFAULT_PASSWORD);
        when(memberRepository.findTopByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 할 수 없다")
    void loginWithWrongPassword(){
        // given
        LoginRequest loginRequest = new LoginRequest(Constant.DAVID_EMAIL, Constant.DEFAULT_PASSWORD);
        Member foundMember = new Member("David", Constant.DAVID_EMAIL, "01044445555", "wrongPassword");
        when(memberRepository.findTopByEmail(loginRequest.getEmail())).thenReturn(Optional.of(foundMember));

        // when
        // then
        Assertions.assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호");
    }
}
