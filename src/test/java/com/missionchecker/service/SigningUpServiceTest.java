package com.missionchecker.service;

import com.missionchecker.domain.Member;
import com.missionchecker.dto.SigningUpRequest;
import com.missionchecker.repository.MemberRepository;
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
class SigningUpServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SigningUpService signingUpService;

    @Test
    @DisplayName("이미 가입된 이메일로 중복 회원가입을 할 수 없다")
    void signingUp() {
        // given
        SigningUpRequest signingUpRequest = new SigningUpRequest("Sophie", "sophie@sophie.com",
                "01022223333", "123456");
        Optional<Member> maybeMember = Optional.of(signingUpRequest.toMember());
        when(memberRepository.findTopByEmail(signingUpRequest.getEmail())).thenReturn(maybeMember);

        // when
        // then
        Assertions.assertThatThrownBy(() -> signingUpService.signUp(signingUpRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

}