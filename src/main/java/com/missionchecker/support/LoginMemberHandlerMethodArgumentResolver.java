package com.missionchecker.support;

import com.missionchecker.domain.Member;
import com.missionchecker.repository.MemberRepository;
import com.missionchecker.util.Constant;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 로그인한 사용자의 경우 세션에 있는 사용자 정보를 꺼내어 핸들러 매개변수에 넣어준다.
 * 핸들러 매개변수로 타입은 SessionMember, 이름은 loginMember여야 동작한다.
 */
@Component
public class LoginMemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String LOGIN_MEMBER = "loginMember";
    private final MemberRepository memberRepository;

    public LoginMemberHandlerMethodArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if(parameter.getParameterType().equals(Member.class) &&
                parameter.getParameter().getName().equals(LOGIN_MEMBER)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        SessionMember sessionMember = (SessionMember) webRequest.getAttribute(Constant.LOGIN_MEMBER, RequestAttributes.SCOPE_SESSION);
        if (sessionMember == null) {
            throw new IllegalStateException("세션 내 로그인한 사용자 정보가 존재하지 않습니다.");
        }
        Member storedMember = memberRepository.getById(sessionMember.getId());
        return storedMember;
    }
}
